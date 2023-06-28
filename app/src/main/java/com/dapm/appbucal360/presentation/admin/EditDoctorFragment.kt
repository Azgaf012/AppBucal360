package com.dapm.appbucal360.presentation.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.databinding.FragmentDoctorEditBinding
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.model.doctor.DoctorState
import com.dapm.appbucal360.presentation.appointment.EditAppointmentViewModel
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.dapm.appbucal360.utils.EnumDoctorStatus
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditDoctorFragment : Fragment() {

    companion object {
        fun newInstance() = EditDoctorFragment()
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()


    private var _binding: FragmentDoctorEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditDoctorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpdateButtonClickListener()
        observeDoctorUpdate()
        sharedViewModel.doctorSelected.value?.let { doctor ->
            fillDoctorData(doctor)
        }
    }

    private fun fillDoctorData(doctor: Doctor) {
        binding.nameDoctor.setText(doctor.name)
        binding.lastNameDoctor.setText(doctor.lastName)
        binding.startTime.setText(doctor.startTime)
        binding.endTime.setText(doctor.endTime)
        doctor.workingDays.forEach { day ->
            when (day) {
                "Monday" -> binding.mondayCheckBox.isChecked = true
                "Tuesday" -> binding.tuesdayCheckBox.isChecked = true
                "Wednesday" -> binding.wednesdayCheckBox.isChecked = true
                "Thursday" -> binding.thursdayCheckBox.isChecked = true
                "Friday" -> binding.fridayCheckBox.isChecked = true
                "Saturday" -> binding.saturdayCheckBox.isChecked = true
                "Sunday" -> binding.sundayCheckBox.isChecked = true
            }
        }

        binding.enabledCheckBox.isChecked = EnumDoctorStatus.ACTIVED.toString().equals(doctor.status)

    }

    private fun setUpdateButtonClickListener() {
        binding.btnRegistrarDoctor.setOnClickListener {
            sharedViewModel.doctorSelected.value?.let { doctor ->
                updateDoctorFromInput(doctor)
            }

        }
    }

    private fun updateDoctorFromInput(doctor: Doctor) {
        val name = binding.nameDoctor.text.toString()
        val lastName = binding.lastNameDoctor.text.toString()
        val startTime = binding.startTime.text.toString()
        val endTime = binding.endTime.text.toString()
        val workingDays = getSelectedWorkingDays()
        val status = if (binding.enabledCheckBox.isChecked) EnumDoctorStatus.ACTIVED else EnumDoctorStatus.RETIRED

        viewModel.updateDoctor(doctor.id.toString(),name, lastName, startTime, endTime, workingDays, status.toString())
    }

    private fun getSelectedWorkingDays(): List<String> {
        return listOfNotNull(
            if (binding.mondayCheckBox.isChecked) "Monday" else null,
            if (binding.tuesdayCheckBox.isChecked) "Tuesday" else null,
            if (binding.wednesdayCheckBox.isChecked) "Wednesday" else null,
            if (binding.thursdayCheckBox.isChecked) "Thursday" else null,
            if (binding.fridayCheckBox.isChecked) "Friday" else null,
            if (binding.saturdayCheckBox.isChecked) "Saturday" else null,
            if (binding.sundayCheckBox.isChecked) "Sunday" else null
        )
    }

    private fun observeDoctorUpdate() {
        viewModel.updateResult.observe(viewLifecycleOwner, Observer { result ->
            handleUpdateResult(result)
        })
    }

    private fun handleUpdateResult(result: DoctorState) {
        when (result) {
            is DoctorState.Success -> navigateToAdminDoctorFragment()
            is DoctorState.Error -> showErrorSnackbar(result.exception)
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

    private fun navigateToAdminDoctorFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_editDoctorFragment_to_adminDoctorsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}