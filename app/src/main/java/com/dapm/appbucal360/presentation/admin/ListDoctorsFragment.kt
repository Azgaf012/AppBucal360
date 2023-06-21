package com.dapm.appbucal360.presentation.admin

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dapm.appbucal360.R

class ListDoctorsFragment : Fragment() {

    companion object {
        fun newInstance() = ListDoctorsFragment()
    }

    private lateinit var viewModel: ListDoctorsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_doctors, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListDoctorsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}