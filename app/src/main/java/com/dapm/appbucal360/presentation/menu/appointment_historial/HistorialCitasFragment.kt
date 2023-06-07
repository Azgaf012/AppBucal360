package com.dapm.appbucal360.presentation.menu.appointment_historial

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dapm.appbucal360.R

class HistorialCitasFragment : Fragment() {

    companion object {
        fun newInstance() = HistorialCitasFragment()
    }

    private lateinit var viewModel: HistorialCitasViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_historial_citas, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HistorialCitasViewModel::class.java)
        // TODO: Use the ViewModel
    }

}