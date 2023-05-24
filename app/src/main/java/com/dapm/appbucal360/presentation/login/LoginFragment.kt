package com.dapm.appbucal360.presentation.login

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById<EditText>(R.id.input_email)
        val passwordEditText = view.findViewById<EditText>(R.id.input_password)
        val loginButton = view.findViewById<Button>(R.id.btn_login)

        val rxtModal = view.findViewById<TextView>(R.id.rxt_modal)
        val modalOlvideContraseña = view.findViewById<LinearLayout>(R.id.modal_olvidéContraseña)

        // Define un OnClickListener para rxtModal
        rxtModal.setOnClickListener {
            // Cuando rxtModal se haga clic, haz modalOlvideContraseña visible
            modalOlvideContraseña.visibility = View.VISIBLE
        }

        modalOlvideContraseña.setOnTouchListener { _, _ ->
            // Consume el evento de toque para evitar que se propague al layout padre
            true
        }

        view.setOnTouchListener { _, event ->
            // Si el modal está visible y se toca fuera de él, ocultarlo
            if (modalOlvideContraseña.visibility == View.VISIBLE && event.action == MotionEvent.ACTION_DOWN) {
                val rect = Rect()
                modalOlvideContraseña.getGlobalVisibleRect(rect)
                if (!rect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    modalOlvideContraseña.visibility = View.GONE
                }
            }
            // No consumir el evento de toque para que otros elementos puedan manejarlo también
            false
        }

        /**[IMPLEMENTAR] Modificar el Gone del modal para que sea visible al hacer clic en olvidé mi contraseña**/

        val registerTextView = view.findViewById<TextView>(R.id.txt_registro)
        registerTextView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.login(email, password)
        }

        // Observar el LiveData loggedInUser
        viewModel.loggedInUser.observe(viewLifecycleOwner, Observer { loggedInUser ->
            // Manejar caso de éxito
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_menuFragment)
        })

        // Observar el LiveData loginError
        viewModel.loginError.observe(viewLifecycleOwner, Observer { exception ->
            // Manejar caso de error
            Snackbar.make(view, exception.localizedMessage ?: "Error al iniciar sesión", Snackbar.LENGTH_LONG).show()

        })

    }
}