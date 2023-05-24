package com.dapm.appbucal360.presentation.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.user.RegistrationState
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById<EditText>(R.id.email_edit_text)
        val passwordEditText = view.findViewById<EditText>(R.id.password_edit_text)
        val firstNameEditText = view.findViewById<EditText>(R.id.firstName_edit_text)
        val lastNameEditText = view.findViewById<EditText>(R.id.lastName_edit_text)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.phoneNumber_edit_text)
        val birthDateEditText = view.findViewById<EditText>(R.id.birthDate_edit_text)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val phoneNumber = phoneNumberEditText.text.toString()

            val birthDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDate = birthDateFormat.parse(birthDateEditText.text.toString())

            viewModel.register(email, password, firstName, lastName, phoneNumber, birthDate)
        }

        // Observar el LiveData registrationState
        viewModel.registrationState.observe(viewLifecycleOwner, Observer { registrationState ->
            when (registrationState) {
                is RegistrationState.Success -> {
                    // Manejar caso de éxito
                    Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is RegistrationState.Error -> {
                    // Manejar caso de error
                    Snackbar.make(view, registrationState.exception?.localizedMessage ?: "Error al registrar", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

}