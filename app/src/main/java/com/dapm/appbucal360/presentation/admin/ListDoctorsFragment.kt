package com.dapm.appbucal360.presentation.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.presentation.common.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListDoctorsFragment : Fragment(), DoctorAdapter.OnDoctorLister {

    private val viewModel: ListDoctorsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctors_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listView = view.findViewById(R.id.listview_doctores)
        observeViewModel()
        viewModel.fetchDoctors()
    }

    private fun observeViewModel() {
        viewModel.doctorsList.observe(viewLifecycleOwner, Observer { doctors ->
            doctors?.let { updateListView(it) }
        })
    }

    private fun updateListView(doctors: List<Doctor>) {
        val doctorsAdapter = DoctorAdapter(requireContext(), doctors, this)
        listView.adapter = doctorsAdapter
    }

    override fun onEdit(doctor: Doctor) {
        sharedViewModel.doctorSelected.value = doctor
        navigateToEditDoctorFragment()
    }

    private fun navigateToEditDoctorFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_adminDoctorsFragment_to_editDoctorFragment)
        }
    }

}