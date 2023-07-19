package com.dapm.appbucal360.presentation.admin

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.databinding.FragmentDoctorsListBinding
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.dapm.appbucal360.utils.showErrorSnackbar
import com.dapm.appbucal360.utils.showSuccessSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListDoctorsFragment : Fragment(), DoctorAdapter.OnDoctorLister {

    private var _binding: FragmentDoctorsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListDoctorsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpdateButtonClickListener()
        viewModel.fetchDoctors()
    }

    private fun observeViewModel() {
        viewModel.doctorsList.observe(viewLifecycleOwner, Observer { doctors ->
            doctors?.let { updateListView(it as MutableList<Doctor>) }
        })

        viewModel.updatedDoctor.observe(viewLifecycleOwner, Observer { updatedDoctor ->
            if (updatedDoctor.isSuccess) {
                requireView().showErrorSnackbar("Doctor habilitado con exito")

            } else {
                requireView().showSuccessSnackbar("Error al habilitar al doctor")
            }
        })
    }

    private fun setUpdateButtonClickListener() {


        binding.backButton.setOnClickListener {
            navigateToMenuAdminFragment()
        }

        binding.addDoctor.setOnClickListener {
            navigateToRegisterDoctorFragment()
        }

        binding.searchDoctor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                (binding.listviewDoctores.adapter as DoctorAdapter).filter.filter(s)
            }

            override fun afterTextChanged(s: Editable) {}
        })

    }

    private fun updateListView(doctors: MutableList<Doctor>) {
        val doctorsAdapter = DoctorAdapter(requireContext(), doctors, this)
        binding.listviewDoctores.adapter = doctorsAdapter
    }

    override fun onEdit(doctor: Doctor) {
        sharedViewModel.doctorSelected.value = doctor
        navigateToEditDoctorFragment()
    }

    override fun onDisabled(doctor: Doctor) {
        TODO("Not yet implemented")
    }

    override fun onEnabled(doctor: Doctor) {
        showConfirmationDialog(doctor)
    }

    private fun navigateToEditDoctorFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_adminDoctorsFragment_to_editDoctorFragment)
        }
    }

    private fun navigateToMenuAdminFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_adminDoctorsFragment_to_menuAdminFragment)
        }
    }

    private fun navigateToRegisterDoctorFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_adminDoctorsFragment_to_registerDoctorFragment)
        }
    }

    private fun showConfirmationDialog(doctor: Doctor) {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage("Esta seguro que desea habilitar al Dr. ${doctor.name}?")
            setPositiveButton("Si") { dialog, _ ->
                doctor.id?.let { viewModel.enableDoctor(it) }
                (binding.listviewDoctores.adapter as DoctorAdapter).updateDoctor(doctor)
            }
            setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

}