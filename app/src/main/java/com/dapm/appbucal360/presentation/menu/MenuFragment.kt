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
        //Usar la línea de bajo para borrar archivo
        //requireContext().deleteFile("login_1")

        val onboardingLayout = view.findViewById<View>(R.id.onboarding_background)
        val screen2 = view.findViewById<View>(R.id.onboarding_2)
        val screen3 = view.findViewById<View>(R.id.onboarding_3)
        val screen4 = view.findViewById<View>(R.id.onboarding_4)
        val btn_screen1_to_screen2 = view.findViewById<Button>(R.id.btn_nxt_screen_1)
        val btn_screen2_to_screen1 = view.findViewById<Button>(R.id.btn_prv_screen_2)
        val btn_screen2_to_screen3 = view.findViewById<Button>(R.id.btn_nxt_screen_2)
        val btn_screen3_to_screen2 = view.findViewById<Button>(R.id.btn_prv_screen_3)
        val btn_screen3_to_screen4 = view.findViewById<Button>(R.id.btn_nxt_screen_3)
        val btn_screen4_to_screen3 = view.findViewById<Button>(R.id.btn_prv_screen_4)
        val btn_screen4_to_menu = view.findViewById<Button>(R.id.btn_finish_screen_4)

         // /**

        if (!archivoLogin.exists()) {
            archivoLogin.createNewFile()
            onboardingLayout.visibility = View.VISIBLE
            Log.d("Login_1","Se creó el archivo")
            btn_screen1_to_screen2.setOnClickListener{
                onboardingLayout.visibility = View.GONE
                screen2.visibility = View.VISIBLE
            }
            btn_screen2_to_screen1.setOnClickListener{
                screen2.visibility = View.GONE
                onboardingLayout.visibility = View.VISIBLE
            }
            btn_screen2_to_screen3.setOnClickListener{
                screen2.visibility = View.GONE
                screen3.visibility = View.VISIBLE
            }
            btn_screen3_to_screen4.setOnClickListener{
                screen3.visibility = View.GONE
                screen4.visibility = View.VISIBLE
            }
            btn_screen3_to_screen2.setOnClickListener{
                screen3.visibility = View.GONE
                screen2.visibility = View.VISIBLE
            }
            btn_screen4_to_screen3.setOnClickListener{
                screen4.visibility = View.GONE
                screen3.visibility = View.VISIBLE
            }
            btn_screen4_to_menu.setOnClickListener{
                screen4.visibility = View.GONE
            }
        } else {
            onboardingLayout.visibility = View.GONE
            Log.d("Login_1","El archivo ya existía")
        }
        // **/

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
