package com.dapm.appbucal360.presentation.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.presentation.common.SharedViewModel

class MenuAdminFragment : Fragment() {

    private val userViewModel: SharedViewModel by activityViewModels()

    companion object {
        fun newInstance() = MenuAdminFragment()
    }

    private lateinit var viewModel: MenuAdminViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombreUsuario: TextView = view.findViewById(R.id.nombreUsuario)
        nombreUsuario.text = "Hola, ${userViewModel.loggedInUser.value?.firstName}"

        val regDoctor = view.findViewById<ImageButton>(R.id.registerDoctor)

        regDoctor.setOnClickListener {
            val action = MenuAdminFragmentDirections.actionMenuAdminFragmentToRegisterDoctorFragment()
            Navigation.findNavController(view).navigate(action)
        }

        val listDoctors = view.findViewById<ImageButton>(R.id.listDoctors)

        listDoctors.setOnClickListener {
            val action = MenuAdminFragmentDirections.actionMenuAdminFragmentToAdminDoctorsFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

}