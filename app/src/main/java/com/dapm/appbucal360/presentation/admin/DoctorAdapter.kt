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
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.utils.EnumDoctorStatus

class DoctorAdapter(
    context: Context,
    private val doctors: MutableList<Doctor>,
    private val listener: OnDoctorLister
    ) : ArrayAdapter<Doctor>(context, R.layout.doctor_list_item, doctors) {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    interface OnDoctorLister{
        fun onEdit(doctor: Doctor)
        fun onDisabled(doctor: Doctor)
        fun onEnabled(doctor: Doctor)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.doctor_list_item, parent, false)

        val doctor = getItem(position)

        // Aquí asignas los datos del doctor a las vistas. Este es un ejemplo y podrías tener más o menos vistas dependiendo de cómo quieras mostrar los doctores.
        val nameView = view.findViewById<TextView>(R.id.doctor_name)
        val doctorStatusView = view.findViewById<TextView>(R.id.doctor_status)
        val desactivarDoctorButton = view.findViewById<ImageButton>(R.id.desactivar_doctor)
        val activarDoctorButton = view.findViewById<ImageButton>(R.id.activar_doctor)

        nameView.text = doctor?.name

        doctorStatusView.text = "HABILITADO"
        doctorStatusView.setTextColor(Color.BLUE)

        if(EnumDoctorStatus.RETIRED.toString().equals(doctor?.state)){
            doctorStatusView.text = "DESHABILITADO"
            doctorStatusView.setTextColor(Color.RED)
        }

        if(EnumDoctorStatus.ACTIVED.toString().equals(doctor?.state)){
            desactivarDoctorButton.visibility = View.VISIBLE
            activarDoctorButton.visibility = View.GONE
        } else {
            desactivarDoctorButton.visibility = View.GONE
            activarDoctorButton.visibility = View.VISIBLE
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
        val desactivarDoctorButton = view.findViewById<ImageButton>(R.id.desactivar_doctor)
        val activarDoctorButton = view.findViewById<ImageButton>(R.id.activar_doctor)

        editButton.setOnClickListener {
            doctor?.let { listener.onEdit(it) }
        }

        desactivarDoctorButton.setOnClickListener {
            doctor?.let { listener.onDisabled(it) }
        }

        activarDoctorButton.setOnClickListener {
            doctor?.let { listener.onEnabled(it) }
        }
    }

    fun updateDoctor(doctor: Doctor) {
        val index = doctors.indexOfFirst { it.id == doctor.id }
        if (index != -1) {
            doctors[index] = doctor
            notifyDataSetChanged()
        }
    }
}