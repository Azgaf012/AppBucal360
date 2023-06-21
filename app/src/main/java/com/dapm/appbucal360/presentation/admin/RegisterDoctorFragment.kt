package com.dapm.appbucal360.presentation.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dapm.appbucal360.R

class RegisterDoctorFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterDoctorFragment()
    }

    private lateinit var viewModel: RegisterDoctorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_doctor, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterDoctorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}