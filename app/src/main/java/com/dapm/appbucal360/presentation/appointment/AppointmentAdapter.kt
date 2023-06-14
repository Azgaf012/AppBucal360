package com.dapm.appbucal360.presentation.appointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.Appointment

class AppointmentAdapter(context: Context, appointments: List<Appointment>) : ArrayAdapter<Appointment>(context, 0, appointments) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_appointment, parent, false)

        val appointment = getItem(position)

        // Aquí puedes obtener referencias a los elementos de la vista en list_item_appointment.xml y establecer los datos de la cita.
        // Por ejemplo:
        val textView = view.findViewById<TextView>(R.id.appointment_doctor)
        textView.text = appointment?.doctor

        // Haz lo mismo para los demás elementos de la vista.

        // Por ejemplo, si tienes botones para editar y eliminar, puedes configurar sus onClickListeners aquí:
        val editButton = view.findViewById<ImageButton>(R.id.editar_cita)
        editButton.setOnClickListener {
            // Aquí puedes manejar el evento de clic en el botón de editar.
        }

        val deleteButton = view.findViewById<ImageButton>(R.id.eliminar_cita)
        deleteButton.setOnClickListener {
            // Aquí puedes manejar el evento de clic en el botón de eliminar.
        }

        return view
    }
}
