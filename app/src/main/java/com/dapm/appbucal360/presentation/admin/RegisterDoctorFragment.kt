package com.dapm.appbucal360.presentation.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.databinding.FragmentRegisterDoctorBinding
import com.dapm.appbucal360.model.doctor.DoctorState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterDoctorFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterDoctorFragment()
    }

    private var _binding: FragmentRegisterDoctorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegisterDoctorViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRegistrationButtonClickListener()
        observeDoctorRegistration()
    }

    private fun setRegistrationButtonClickListener() {
        binding.btnRegistrarDoctor.setOnClickListener {
            registerDoctorFromInput()
        }
    }

    private fun registerDoctorFromInput() {
        val name = binding.nameDoctor.text.toString()
        val lastName = binding.lastNameDoctor.text.toString()
        val startTime = binding.startTime.text.toString()
        val endTime = binding.endTime.text.toString()
        val workingDays = getSelectedWorkingDays()

        viewModel.registerDoctor(name, lastName, startTime, endTime, workingDays)
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

    private fun observeDoctorRegistration() {
        viewModel.registerResult.observe(viewLifecycleOwner, Observer { result ->
            handleRegistrationResult(result)
        })
    }

    private fun handleRegistrationResult(result: DoctorState) {
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
                .navigate(R.id.action_registerDoctorFragment_to_adminDoctorsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}