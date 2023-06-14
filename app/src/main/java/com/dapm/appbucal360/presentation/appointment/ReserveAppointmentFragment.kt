package com.dapm.appbucal360.presentation.appointment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.DatePicker
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.AppointmentState
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.dapm.appbucal360.presentation.login.LoginViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ReserveAppointmentFragment : Fragment() {

    private val viewModel: ReserveAppointmentViewModel by viewModels()
    private val userViewModel: SharedViewModel by activityViewModels()

    private lateinit var doctorNameAutocomplete: AutoCompleteTextView
    private lateinit var reserveButton: Button
    private lateinit var timeAutocomplete: AutoCompleteTextView
    private lateinit var selectedDate:Date
    private lateinit var textDate: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reserve_appointment, container, false)
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
        textDate = view.findViewById(R.id.text_date)
        val datePickerButton = view.findViewById<Button>(R.id.btn_date_picker)
        datePickerButton.setOnClickListener {
            showDatePicker()
        }
    }

    private fun setupOnClickListeners() {
        reserveButton.setOnClickListener {
            val selectedDoctor = doctorNameAutocomplete.text.toString()
            val selectedTime = timeAutocomplete.text.toString()

            val loggedInUser = userViewModel.loggedInUser.value

            if (selectedDoctor.isNotEmpty() && selectedDate != null && selectedTime.isNotEmpty() && loggedInUser != null) {
                viewModel.registerAppointment(selectedDoctor, selectedDate, loggedInUser, selectedTime)
            } else {
                Snackbar.make(
                    requireView(),
                    "Por favor, selecciona un doctor, una fecha y un horario",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }

    private fun observeViewModel() {
        viewModel.registerResult.observe(viewLifecycleOwner, Observer { appointmentState ->
            when (appointmentState) {
                is AppointmentState.Success -> navigateToCitasFragment()
                is AppointmentState.Error -> showErrorSnackbar(appointmentState.exception)
            }
        })

        viewModel.doctorsList.observe(viewLifecycleOwner, Observer { doctors ->
            setupDoctorsAutocomplete(doctors)
        })
    }

    private fun navigateToCitasFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_reservationFragment_to_citasFragment)
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

    private fun showDatePicker() {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(
                CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()
            )

        val datePicker = datePickerBuilder.build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate = Date(selection) // Aquí es donde inicializas 'selectedDate'
            val utcFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")
            val localFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dateInUTC = utcFormat.format(selectedDate)
            val parsedDate = localFormat.parse(dateInUTC)
            textDate.text = localFormat.format(parsedDate)
        }

        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun setupDoctorsAutocomplete(doctors: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, doctors)
        doctorNameAutocomplete.setAdapter(adapter)
    }
}
