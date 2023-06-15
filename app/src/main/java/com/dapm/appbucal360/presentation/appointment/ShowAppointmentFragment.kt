package com.dapm.appbucal360.presentation.appointment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowAppointmentFragment : Fragment(), AppointmentAdapter.OnAppointmentListener {

    private val viewModel: ShowAppointmentViewModel by viewModels()
    private val userViewModel: SharedViewModel by activityViewModels()

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_appointment_show, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setupOnClickListeners()
        observeViewModel()
    }

    private fun initViews(view: View) {
        listView = view.findViewById(R.id.listview_citas)
    }

    private fun setupOnClickListeners() {
        // Aquí puedes configurar los listeners para los botones de editar y eliminar
    }

    private fun observeViewModel() {
        viewModel.appointments.observe(viewLifecycleOwner, Observer { result ->
            if (result.isSuccess) {
                result.getOrNull()?.let { updateListView(it) }
            } else {
                result.exceptionOrNull()?.let { showErrorSnackbar(it.message ?: "Error al obtener citas") }
            }
        })

        viewModel.deleteResult.observe(viewLifecycleOwner, Observer { result ->
            if (result.isSuccess) {
                showDeleteSuccessSnackbar()
            } else {
                result.exceptionOrNull()?.let { showErrorSnackbar(it.message ?: "Error al eliminar cita") }
            }
        })

        userViewModel.loggedInUser.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                viewModel.fetchAppointments(it.id)
            }
        })
    }

    private fun updateListView(appointments: List<Appointment>) {
        val mutableAppointments = appointments.toMutableList()
        val adapter = AppointmentAdapter(requireContext(), mutableAppointments, this)
        listView.adapter = adapter
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun showDeleteSuccessSnackbar() {
        Snackbar.make(
            requireView(),
            "Cita eliminada con éxito",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onEdit(appointment: Appointment) {
        TODO("Not yet implemented")
    }

    override fun onDelete(appointment: Appointment) {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar esta cita?")
            .setPositiveButton("Sí") { _, _ ->
                appointment.id?.let { viewModel.deleteAppointment(it) }
                (listView.adapter as AppointmentAdapter).removeAppointment(appointment)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
