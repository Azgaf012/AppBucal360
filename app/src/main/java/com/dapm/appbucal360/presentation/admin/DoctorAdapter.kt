package com.dapm.appbucal360.presentation.admin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.doctor.Doctor

class DoctorAdapter(context: Context, private val doctors: List<Doctor>) : ArrayAdapter<Doctor>(context, R.layout.doctor_list_item, doctors) {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.doctor_list_item, parent, false)

        val doctor = getItem(position)

        // Aquí asignas los datos del doctor a las vistas. Este es un ejemplo y podrías tener más o menos vistas dependiendo de cómo quieras mostrar los doctores.
        val nameView = view.findViewById<TextView>(R.id.doctor_name)
        nameView.text = doctor?.name

        // Puedes hacer lo mismo para otros datos del Doctor

        return view
    }

    override fun getItem(position: Int): Doctor {
        return doctors[position]
    }

    override fun getCount(): Int {
        return doctors.size
    }
}