package com.dapm.appbucal360.presentation.appointment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dapm.appbucal360.R
import com.dapm.appbucal360.model.appointment.Appointment
import com.dapm.appbucal360.model.doctor.Doctor
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAdapter(
    context: Context,
    appointments: MutableList<Appointment>,
    private val listener: OnAppointmentListener
) : ArrayAdapter<Appointment>(context, 0, appointments), Filterable {

    private val allAppointments = ArrayList(appointments)

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
        textDoctorView.text = "${appointment?.doctor?.name} ${appointment?.doctor?.lastName}" ?: ""

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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList: MutableList<Appointment> = ArrayList()

                if (constraint == null || constraint.isEmpty()) {
                    filteredList.addAll(allAppointments)
                } else {
                    val filterPattern = constraint.toString().lowercase(Locale.getDefault()).trim { it <= ' ' }

                    for (appointment in allAppointments) {
                        if (appointment.doctor?.name?.lowercase(Locale.getDefault())?.contains(filterPattern) == true) {
                            filteredList.add(appointment)
                        }
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                addAll(results?.values as List<Appointment>)
                notifyDataSetChanged()
            }
        }
    }
}
