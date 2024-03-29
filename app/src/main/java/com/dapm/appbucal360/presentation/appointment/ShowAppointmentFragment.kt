package com.dapm.appbucal360.presentation.appointment

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.databinding.FragmentAppointmentShowBinding
import com.dapm.appbucal360.databinding.FragmentDoctorsListBinding
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.presentation.admin.DoctorAdapter
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowAppointmentFragment : Fragment(), AppointmentAdapter.OnAppointmentListener {

    private var _binding: FragmentAppointmentShowBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ShowAppointmentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAppointmentShowBinding.inflate(inflater, container, false)
        return binding.root
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
        binding.backButton.setOnClickListener {
            navigateToMenuFragment()
        }

        binding.fabAddAppointment.setOnClickListener {
            navigateToReserveAppointmentFragment()
        }

        binding.searchDoctor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                (binding.listviewCitas.adapter as? AppointmentAdapter)?.filter?.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })
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

        sharedViewModel.loggedInUser.observe(viewLifecycleOwner, Observer { user ->
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

    private fun navigateToEditAppointmentFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_showAppointmentFragment_to_editAppointmentFragment)
        }
    }

    private fun navigateToReserveAppointmentFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_showAppointmentFragment_to_reserveAppointmentFragment)
        }
    }

    private fun navigateToMenuFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_showAppointmentFragment_to_menuFragment)
        }
    }

    override fun onEdit(appointment: Appointment) {
        sharedViewModel.appointmentSelected.value = appointment
        navigateToEditAppointmentFragment()

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
