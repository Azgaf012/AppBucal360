package com.dapm.appbucal360.presentation.menu.citas_list

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.dapm.appbucal360.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList

class CitasFragment : Fragment() {
    private var listView: ListView? = null
    private var citas: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_citas, container, false)

        listView = rootView.findViewById(R.id.listview_citas)
        citas = ArrayList()

        val db = FirebaseFirestore.getInstance()
        val citasCollection = db.collection("citas")

        citasCollection.get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot? ->
                querySnapshot?.let {
                    for (document in querySnapshot) {
                        val fechaCita = document.getTimestamp("fecha_cita")
                        val nombreDoctorCita = document.getString("nombre_doctor_cita")
                        fechaCita?.let {
                            val fechaTexto = formatDate(it)
                            val citaTexto = "$fechaTexto\nDoctor: $nombreDoctorCita"
                            citas!!.add(citaTexto)
                        }
                    }

                    val adapter = CitasAdapter(
                        requireContext(),
                        citas!!
                    )
                    listView?.adapter = adapter
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el caso de error aquí
            }

        return rootView
    }

    private fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()

        // Obtener el día del mes y el mes actual en español
        val dayOfMonth = SimpleDateFormat("d", Locale("es", "ES")).format(date)
        val month = SimpleDateFormat("MMMM", Locale("es", "ES")).format(date).capitalize()

        // Obtener la hora y el minuto en formato de 12 horas
        val hourMinuteFormat = SimpleDateFormat("h:mm", Locale("es", "ES"))
        val time = hourMinuteFormat.format(date)
        val amPm = if (date.hours < 12) "a.m." else "p.m."
        val formattedTime = "$time$amPm"

        // Concatenar la fecha y hora en el formato deseado
        val formattedDate = "$dayOfMonth de $month $formattedTime"

        return formattedDate
    }

    private class CitasAdapter(context: Context, citas: ArrayList<String>) :
        ArrayAdapter<String>(context, 0, citas) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            val cita = getItem(position)

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_cita, parent, false)
            }

            val textView = view?.findViewById<TextView>(R.id.text1)
            val button = view?.findViewById<Button>(R.id.button1)

            textView?.text = cita
            textView?.setTextColor(Color.BLACK)
            textView?.setTypeface(null, Typeface.BOLD)
            textView?.textSize = 18f
            textView?.setPadding(50, 10, 0, 10)

            button?.setOnClickListener {
                // Acción que se realizará al hacer clic en el botón
                // Puedes implementar aquí la redirección o cualquier otra lógica que desees
            }

            return view!!
        }
    }
}