package com.dapm.appbucal360.presentation.menu.reservation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.AppointmentState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import java.util.Date

class reservationFragment : Fragment() {

    private val viewModel: ReservationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val doctorNameTextView = view.findViewById<Spinner>(R.id.spinner_doctores_disponibles)
        val patientNameEditText = "Prueba"//view.findViewById<TextInputEditText>(R.id.inputNombrePaciente)
        val patientContactNumberEditText = "999888999"//view.findViewById<TextInputEditText>(R.id.inputNumeroContactoPaciente)
        val reserveButton = view.findViewById<Button>(R.id.btn_confirmarReservation)

        reserveButton.setOnClickListener {
            val doctor = doctorNameTextView.selectedItem.toString()
            val patientName = "Prueba"
            val patientContactNumber = "999888999"

            val date = Date()  // Debes obtener la fecha de algún lado. Por ahora, solo estoy utilizando la fecha actual.

            viewModel.registerAppointment(doctor, date)
        }

        viewModel.registerResult.observe(viewLifecycleOwner, Observer { appointmentState ->
            when (appointmentState) {
                is AppointmentState.Success -> {
                    Navigation.findNavController(view).navigate(R.id.action_reservationFragment_to_citasFragment)
                }
                is AppointmentState.Error -> {
                    // Handle error case
                    // Here you have your exception in appointmentState.exception
                    Snackbar.make(view, appointmentState.exception.localizedMessage ?: "Error al registrar", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}