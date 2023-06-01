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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import kotlin.collections.ArrayList

class CitasFragment : Fragment() {
    private var listView: ListView? = null
    private var citas: ArrayList<String>? = null
    private var citasIds: ArrayList<String>? = null
    private var adapter: CitasAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_citas, container, false)

        listView = rootView.findViewById(R.id.listview_citas)
        citas = ArrayList()
        citasIds = ArrayList()

        adapter = CitasAdapter(
            requireContext(),
            citas!!,
            citasIds!! // Pasar la lista de IDs al adaptador
        )
        listView?.adapter = adapter

        val db = FirebaseFirestore.getInstance()
        val citasCollection = db.collection("citas")

        citasCollection.get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot? ->
                querySnapshot?.let {
                    citas?.clear()
                    citasIds?.clear()

                    for (document in querySnapshot) {
                        val id = document.id // Obtener el ID del documento
                        val fechaCita = document.getTimestamp("fecha_cita")
                        val nombreDoctorCita = document.getString("nombre_doctor_cita")
                        fechaCita?.let {
                            val fechaTexto = formatDate(it)
                            val citaTexto = "$fechaTexto\nDoctor: $nombreDoctorCita"
                            citas?.add(citaTexto)
                            citasIds?.add(id) // Agregar el ID a la lista de IDs
                        }
                    }

                    adapter?.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el caso de error aquí
                // Por ejemplo, mostrar un mensaje de error al usuario
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

    private inner class CitasAdapter(
        context: Context,
        citas: ArrayList<String>,
        ids: ArrayList<String>
    ) :
        ArrayAdapter<String>(context, 0, citas) {

        private val citasIds: ArrayList<String> = ids

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var view = convertView
            val cita = getItem(position)

            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.list_item_cita, parent, false)
            }

            val textView = view?.findViewById<TextView>(R.id.text1)
            val button = view?.findViewById<ImageButton>(R.id.eliminar_cita)

            textView?.text = cita
            textView?.setTextColor(Color.BLACK)
            textView?.setTypeface(null, Typeface.BOLD)
            textView?.textSize = 18f
            textView?.setPadding(50, 10, 0, 10)

            // Verificar si la posición es válida y no está fuera de límites
            if (position >= 0 && position < count && position < citasIds.size) {
                val citaId = citasIds[position] // Obtener el ID correspondiente a la posición

                button?.setOnClickListener {
                    showConfirmationDialog(citaId, position)
                }
            }

            return view!!
        }

        private fun showConfirmationDialog(citaId: String, position: Int) {
            val alertDialogBuilder = AlertDialog.Builder(context)
            alertDialogBuilder.setTitle("Eliminación de cita")
            alertDialogBuilder.setMessage("¿Estás seguro de que quieres eliminar esta cita?")
            alertDialogBuilder.setPositiveButton("Sí") { _, _ ->
                eliminarCita(citaId, position)
            }
            alertDialogBuilder.setNegativeButton("No", null)
            alertDialogBuilder.show()
        }

        private fun eliminarCita(citaId: String, position: Int) {
            val db = FirebaseFirestore.getInstance()
            val citasCollection = db.collection("citas")

            citasCollection.document(citaId)
                .delete()
                .addOnSuccessListener {
                    // Eliminación exitosa, realizar las acciones necesarias (redirección, actualización, etc.)
                    citasIds.removeAt(position)
                    notifyDataSetChanged()       //NO FUNCIONA EL REFRESCAR LA LISTA
                    requireActivity().recreate()   //REFRESCA LA VISTA COMPLETO, SOLUCION TEMPORAL
                }
                .addOnFailureListener { exception ->
                    // Manejar el caso de error aquí
                    // Por ejemplo, mostrar un mensaje de error al usuario
                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val back = view.findViewById<ImageButton>(R.id.imageButton)

        back.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_citasFragment_to_menuFragment)
        }
    }
}