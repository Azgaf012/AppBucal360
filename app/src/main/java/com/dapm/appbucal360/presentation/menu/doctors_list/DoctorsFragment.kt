package com.dapm.appbucal360.presentation.menu.doctors_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dapm.appbucal360.R

class DoctorsFragment : Fragment() {

    companion object {
        fun newInstance() = DoctorsFragment()
    }

    private lateinit var viewModel: DoctorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doctors, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DoctorsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}