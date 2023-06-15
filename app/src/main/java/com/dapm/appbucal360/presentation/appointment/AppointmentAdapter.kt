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
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(
    context: Context,
    appointments: MutableList<Appointment>,
    private val listener: OnAppointmentListener
) : ArrayAdapter<Appointment>(context, 0, appointments) {

    interface OnAppointmentListener {
        fun onEdit(appointment: Appointment)
        fun onDelete(appointment: Appointment)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflateView(parent)
        val appointment = getItem(position)

        bindAppointmentDataToView(view, appointment)
        setButtonListeners(view, appointment)

        return view
    }

    private fun inflateView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.appointment_list_item, parent, false)
    }

    private fun bindAppointmentDataToView(view: View, appointment: Appointment?) {
        val textDoctorView = view.findViewById<TextView>(R.id.appointment_doctor)
        textDoctorView.text = appointment?.doctor

        val dateTimeTextView = view.findViewById<TextView>(R.id.appointment_date_time)
        dateTimeTextView.text = formatAppointmentDateTime(appointment)
    }

    private fun formatAppointmentDateTime(appointment: Appointment?): String {
        val utcFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        return "${utcFormat.format(appointment?.date)} - ${appointment?.time}"
    }

    private fun setButtonListeners(view: View, appointment: Appointment?) {
        val editButton = view.findViewById<ImageButton>(R.id.editar_cita)
        editButton.setOnClickListener {
            appointment?.let { listener.onEdit(it) }
        }

        val deleteButton = view.findViewById<ImageButton>(R.id.eliminar_cita)
        deleteButton.setOnClickListener {
            appointment?.let { listener.onDelete(it) }
        }
    }

    fun removeAppointment(appointment: Appointment) {
        remove(appointment)
        notifyDataSetChanged()
    }
}
