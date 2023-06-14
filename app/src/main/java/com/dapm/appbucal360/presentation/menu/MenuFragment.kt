package com.dapm.appbucal360.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.dapm.appbucal360.R
import com.dapm.appbucal360.presentation.common.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    @Deprecated("Deprecated in Java")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nombreUsuario: TextView = view.findViewById(R.id.nombreUsuario)
        nombreUsuario.text = "Hola, ${userViewModel.loggedInUser.value?.firstName}"

        val listCitas = view.findViewById<ImageButton>(R.id.secondOptionHome)

        listCitas.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToCitasFragment()
            Navigation.findNavController(view).navigate(action)
        }

        val regCita = view.findViewById<ImageButton>(R.id.firstOptionHome)

        regCita.setOnClickListener {
            val action = MenuFragmentDirections.actionMenuFragmentToRegCitaFragment()
            Navigation.findNavController(view).navigate(action)
        }

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        // TODO: Use the ViewModel
    }

}