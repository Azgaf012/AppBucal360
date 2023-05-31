package com.dapm.appbucal360.presentation.menu.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.AppointmentState
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*


class reservationFragment : Fragment() {

    private val viewModel: ReservationViewModel by viewModels()
    private lateinit var doctorNameSpinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doctorNameSpinner = view.findViewById(R.id.spinner_doctores_disponibles)

        val patientNameEditText = "Prueba"//view.findViewById<TextInputEditText>(R.id.inputNombrePaciente)
        val patientContactNumberEditText = "999888999"//view.findViewById<TextInputEditText>(R.id.inputNumeroContactoPaciente)
        val reserveButton = view.findViewById<Button>(R.id.btn_confirmarReservation)

        fetchDataFromFirestore()

        reserveButton.setOnClickListener {
            val doctor = doctorNameSpinner.selectedItem.toString()
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

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        val collectionRef = db.collection("doctor")

        collectionRef
            .whereEqualTo("estado_doctor", "A")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val dataList = ArrayList<String>()
                dataList.add("Seleccione un doctor") // Agregar el placeholder al inicio de la lista

                for (documentSnapshot in querySnapshot) {
                    val nombreDoctor = documentSnapshot.getString("nombre_doctor")
                    val apellidoDoctor = documentSnapshot.getString("apellido_doctor")

                    if (nombreDoctor != null && apellidoDoctor != null) {
                        val doctorFullName = "$nombreDoctor $apellidoDoctor"
                        dataList.add(doctorFullName)
                    }
                }

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dataList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                doctorNameSpinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Manejo de errores si ocurre alguno al obtener los datos
            }
    }
}
