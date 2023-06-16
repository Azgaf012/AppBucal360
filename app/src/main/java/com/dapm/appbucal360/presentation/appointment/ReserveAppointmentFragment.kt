package com.dapm.appbucal360.presentation.appointment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.AppointmentState
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.dapm.appbucal360.utils.CalendarViewAppointmentFragment
import com.dapm.appbucal360.utils.DisableDaysDecorator
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.*

@AndroidEntryPoint
class ReserveAppointmentFragment : CalendarViewAppointmentFragment() {

    private val viewModel: ReserveAppointmentViewModel by viewModels()
    private val userViewModel: SharedViewModel by activityViewModels()

    private lateinit var doctorNameAutocomplete: AutoCompleteTextView
    private lateinit var reserveButton: FloatingActionButton
    private lateinit var selectedDate: Date
    private var selectedDoctor: Doctor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_reserve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        viewModel.fetchDoctors()
        setupOnClickListeners()
        observeViewModel()
    }

    private fun initViews(view: View) {
        doctorNameAutocomplete = view.findViewById(R.id.autocomplete_doctores_disponibles)
        reserveButton = view.findViewById(R.id.btn_confirmarReservation)
        timeAutocomplete = view.findViewById(R.id.autocomplete_horas_disponibles)
        calendarView = view.findViewById(R.id.calendarView)

        val calendar = Calendar.getInstance()
        val today = CalendarDay.from(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        calendarView?.state()?.edit()
            ?.setMinimumDate(today)
            ?.commit()

    }

    private fun setupOnClickListeners() {
        reserveButton.setOnClickListener {

            val selectedTime = timeAutocomplete?.text.toString()
            val loggedInUser = userViewModel.loggedInUser.value

            if (selectedDoctor != null && selectedTime.isNotEmpty() && loggedInUser != null && ::selectedDate.isInitialized) {

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmar de reserva")
                    .setMessage("¿Estás seguro de que desea registrar esta cita?")
                    .setPositiveButton("Sí") { _, _ ->
                        val utcFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
                        val selectedTextDate = utcFormat.format(selectedDate)
                        viewModel.registerAppointment(
                            selectedDoctor!!,
                            selectedTextDate,
                            loggedInUser,
                            selectedTime
                        )
                    }
                    .setNegativeButton("No", null)
                    .show()
            } else {
                Snackbar.make(
                    requireView(),
                    "Por favor, selecciona un doctor, una fecha y un horario",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        }

        calendarView?.setOnDateChangedListener { widget, date, selected ->
            val calendar = Calendar.getInstance()
            calendar.set(date.year, date.month - 1, date.day)
            selectedDate = calendar.time
        }

    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(viewLifecycleOwner, Observer { appointmentState ->
            when (appointmentState) {
                is AppointmentState.Success -> navigateToShowAppointmentFragment()
                is AppointmentState.Error -> showErrorSnackbar(appointmentState.exception)
            }
        })

        viewModel.doctorsList.observe(viewLifecycleOwner, Observer { doctors ->
            setupDoctorsAutocomplete(doctors)
        })
    }

    private fun navigateToShowAppointmentFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_reserveAppointmentFragment_to_showAppointmentFragment)
        }
    }

    private fun showErrorSnackbar(exception: Exception) {
        view?.let {
            Snackbar.make(
                it,
                exception.localizedMessage ?: "Error al registrar",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun setupDoctorsAutocomplete(doctors: List<Doctor>) {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, doctors)
        doctorNameAutocomplete.setAdapter(adapter)

        doctorNameAutocomplete.setOnItemClickListener { parent, view, position, id ->
            selectedDoctor = parent.getItemAtPosition(position) as Doctor

            // Actualizar el CalendarView y el AutoCompleteTextView para las horas disponibles
            updateCalendarView(selectedDoctor!!)
            updateTimeAutocomplete(selectedDoctor!!)
        }
    }

}
