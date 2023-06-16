package com.dapm.appbucal360.presentation.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.appointment.AppointmentState
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.dapm.appbucal360.utils.CalendarViewAppointmentFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EditAppointmentFragment : CalendarViewAppointmentFragment(){

    private val viewModel: EditAppointmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var reserveButton: FloatingActionButton
    private lateinit var doctorNameTextView: TextView
    private lateinit var selectedDate: Date
    private var selectedDoctor: Doctor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupOnClickListeners()
        observeViewModel()
    }

    private fun initViews(view: View) {
        doctorNameTextView = view.findViewById(R.id.textview_doctor_name)
        reserveButton = view.findViewById(R.id.btn_confirmarEdicion)
        timeAutocomplete = view.findViewById(R.id.autocomplete_horas_disponibles)
        calendarView = view.findViewById(R.id.calendarView)
        selectedDoctor = sharedViewModel.appointmentSelected.value?.doctor

        updateCalendarView(selectedDoctor!!)
        updateTimeAutocomplete(selectedDoctor!!)

        val calendar = Calendar.getInstance()
        val today = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        calendarView?.state()?.edit()
            ?.setMinimumDate(today)
            ?.commit()
    }

    private fun setupOnClickListeners() {
        reserveButton.setOnClickListener {
            sharedViewModel.appointmentSelected.value?.let { appointment ->

                val newTime = timeAutocomplete?.text.toString()
                if ( newTime.isNotEmpty() && appointment != null && ::selectedDate.isInitialized) {

                    val updatedAppointment = appointment.copy(time = newTime, date = selectedDate)
                    viewModel.updateAppointment(updatedAppointment)
                }else {
                    Snackbar.make(
                        requireView(),
                        "Por favor una fecha y un horario",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }

        calendarView?.setOnDateChangedListener { widget, date, selected ->
            val calendar = Calendar.getInstance()
            calendar.set(date.year, date.month - 1, date.day)
            selectedDate = calendar.time
        }
    }

    private fun observeViewModel() {
        viewModel.updateResult.observe(viewLifecycleOwner, Observer { appointmentState ->
            when (appointmentState) {
                is AppointmentState.Success -> {
                    showUpdateSuccessSnackbar()
                    navigateToShowAppointmentFragment()
                }
                is AppointmentState.Error -> showErrorSnackbar(appointmentState.exception)
            }
        })

        sharedViewModel.appointmentSelected.observe(viewLifecycleOwner, Observer { appointment ->
            appointment?.let {
                updateAppointmentDetails(it)
            }
        })
    }

    private fun navigateToShowAppointmentFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_editAppointmentFragment_to_showAppointmentFragment)
        }
    }

    private fun updateAppointmentDetails(appointment: Appointment) {
        doctorNameTextView.text = "${appointment?.doctor?.name} ${appointment?.doctor?.lastName}" ?: ""
        // Aquí puedes actualizar los otros detalles de la cita (fecha, hora, etc.)
    }

    private fun showErrorSnackbar(exception: Exception) {
        view?.let {
            Snackbar.make(
                it,
                exception.localizedMessage ?: "Error al actualizar",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun showUpdateSuccessSnackbar() {
        Snackbar.make(
            requireView(),
            "Cita actualizada con éxito",
            Snackbar.LENGTH_LONG
        ).show()
    }

}