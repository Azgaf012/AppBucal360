package com.dapm.appbucal360.presentation.admin

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.utils.EnumDoctorStatus

class DoctorAdapter(
    context: Context,
    private val doctors: List<Doctor>,
    private val listener: OnDoctorLister
    ) : ArrayAdapter<Doctor>(context, R.layout.doctor_list_item, doctors) {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    interface OnDoctorLister{
        fun onEdit(doctor: Doctor)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.doctor_list_item, parent, false)

        val doctor = getItem(position)

        // Aquí asignas los datos del doctor a las vistas. Este es un ejemplo y podrías tener más o menos vistas dependiendo de cómo quieras mostrar los doctores.
        val nameView = view.findViewById<TextView>(R.id.doctor_name)
        val doctorStatusView = view.findViewById<TextView>(R.id.doctor_status)
        nameView.text = doctor?.name

        doctorStatusView.text = "HABILITADO"
        doctorStatusView.setTextColor(Color.BLUE)

        if(EnumDoctorStatus.RETIRED.toString().equals(doctor?.status)){
            doctorStatusView.text = "DESHABILITADO"
            doctorStatusView.setTextColor(Color.RED)
        }


        setButtonListeners(view, doctor)

        return view
    }

    override fun getItem(position: Int): Doctor {
        return doctors[position]
    }

    override fun getCount(): Int {
        return doctors.size
    }

    private fun setButtonListeners(view: View, doctor: Doctor?) {
        val editButton = view.findViewById<ImageButton>(R.id.editar_doctor)
        editButton.setOnClickListener {
            doctor?.let { listener.onEdit(it) }
        }
    }
}