package com.dapm.appbucal360.presentation.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.presentation.common.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private val userViewModel: SharedViewModel by activityViewModels()
    companion object {
        fun newInstance() = MenuFragment()
    }

    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val archivoLogin = File(requireContext().filesDir, "login_1")

        if (!archivoLogin.exists()) {
            archivoLogin.createNewFile()
            val onboardingLayout = view.findViewById<View>(R.id.onboarding_background)
            onboardingLayout.visibility = View.VISIBLE
            Log.d("No Existía","Existía")
        } else {
            val onboardingLayout = view.findViewById<View>(R.id.onboarding_background)
            onboardingLayout.visibility = View.GONE
            Log.d("Sí Existe","Existe")
        }

        val nombreUsuario: TextView = view.findViewById(R.id.nombreUsuario)
        nombreUsuario.text = "Hola, ${userViewModel.loggedInUser.value?.firstName}"

        val listCitas = view.findViewById<ImageButton>(R.id.listDoctors)

        listCitas.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToShowAppointmentFragment()
            Navigation.findNavController(view).navigate(action)
        }

        val regCita = view.findViewById<ImageButton>(R.id.registerDoctor)

        regCita.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToReserveAppointmentFragment()
            Navigation.findNavController(view).navigate(action)
        }

        //Barra inferior funcionamiento

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
