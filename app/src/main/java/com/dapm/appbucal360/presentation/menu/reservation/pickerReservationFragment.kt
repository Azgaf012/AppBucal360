package com.dapm.appbucal360.presentation.menu.reservation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dapm.appbucal360.R

class pickerReservationFragment : Fragment() {

    companion object {
        fun newInstance() = pickerReservationFragment()
    }

    private lateinit var viewModel: PickerReservationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_picker_reservation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PickerReservationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}