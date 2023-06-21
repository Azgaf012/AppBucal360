package com.dapm.appbucal360.presentation.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.doctor.Doctor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListDoctorsFragment : Fragment() {

    private val viewModel: ListDoctorsViewModel by viewModels()
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_doctors, container, false)
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
        val doctorsAdapter = DoctorAdapter(requireContext(), doctors)
        listView.adapter = doctorsAdapter
    }

}