package com.dapm.appbucal360.presentation.menu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.dapm.appbucal360.R

class MenuFragment : Fragment() {

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

        val listCitas = view.findViewById<ImageButton>(R.id.secondOptionHome)

        listCitas.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_citasFragment)
        }

        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        // TODO: Use the ViewModel
    }

}