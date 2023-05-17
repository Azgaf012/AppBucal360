package com.dapm.appbucal360.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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