package com.dapm.appbucal360.presentation.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.presentation.appointment.ShowAppointmentViewModel
import com.dapm.appbucal360.presentation.common.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private val userViewModel: SharedViewModel by activityViewModels()
    private val viewModelMenu: MenuViewModel by viewModels()
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
        setupView(view)
        handleButtonsClick(view)
    }

    private fun setupView(view: View) {
        val archivoLogin = File(requireContext().filesDir, "login_1")
        val onboardingLayout = view.findViewById<View>(R.id.onboarding_background)
        if (!archivoLogin.exists()) {
            archivoLogin.createNewFile()
            onboardingLayout.visibility = View.VISIBLE
            Log.d("Login_1","Se creó el archivo")
        } else {
            onboardingLayout.visibility = View.GONE
            Log.d("Login_1","El archivo ya existía")
        }

        val nombreUsuario: TextView = view.findViewById(R.id.nombreUsuario)
        nombreUsuario.text = "Hola, ${userViewModel.loggedInUser.value?.firstName}"

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
    }

    private fun handleButtonsClick(view: View) {
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

        val logout = view.findViewById<ImageButton>(R.id.buttonSessionClose)
        logout.setOnClickListener {
            viewModelMenu.logout()

            // Aquí puedes navegar de nuevo a la pantalla de inicio de sesión
            val action = MenuFragmentDirections.actionMenuFragmentToLoginFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }
}
