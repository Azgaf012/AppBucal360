package com.dapm.appbucal360.presentation.admin

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.dapm.appbucal360.R
import com.dapm.appbucal360.databinding.FragmentDoctorEditBinding
import com.dapm.appbucal360.model.doctor.Doctor
import com.dapm.appbucal360.model.doctor.DoctorState
import com.dapm.appbucal360.presentation.common.SharedViewModel
import com.dapm.appbucal360.utils.EnumDayOfWeek
import com.dapm.appbucal360.utils.EnumDoctorStatus
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class EditDoctorFragment : Fragment() {

    companion object {
        fun newInstance() = EditDoctorFragment()
    }

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var doctorWorkingDays: List<String>

    private var _binding: FragmentDoctorEditBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditDoctorViewModel by viewModels()

    private val selectedDays = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDoctorEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpdateButtonClickListener()
        observeDoctorUpdate()
        sharedViewModel.doctorSelected.value?.let { doctor ->
            fillDoctorData(doctor)
        }
        binding.days.setOnClickListener { showMultiChoiceDialog() }
    }

    private fun fillDoctorData(doctor: Doctor) {
        binding.nameDoctor.setText(doctor.name)
        binding.lastNameDoctor.setText(doctor.lastName)

        binding.startTime.setText(doctor.startTime ?: "")
        binding.endTime.setText(doctor.endTime ?: "")

        doctorWorkingDays = sharedViewModel.doctorSelected.value?.workingDays!!.map {
            EnumDayOfWeek.valueOf(it.uppercase(Locale.ROOT)).displayName
        } ?: listOf()

        selectedDays.addAll(doctor.workingDays.map { EnumDayOfWeek.valueOf(it.uppercase()).displayName })

        binding.days.setText(selectedDays.joinToString())

        binding.enabledSwitch.isChecked = EnumDoctorStatus.ACTIVED.toString().equals(doctor.state)

    }

    private fun showMultiChoiceDialog() {
        val daysOfWeek = EnumDayOfWeek.values().map { it.displayName }

        val checkedItems = BooleanArray(daysOfWeek.size) {
            doctorWorkingDays.contains(daysOfWeek[it])
        }

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
        }

        val checkBoxes = daysOfWeek.mapIndexed { index, day ->
            CheckBox(context).apply {
                text = day
                isChecked = checkedItems[index]
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        selectedDays.add(day)
                    } else {
                        selectedDays.remove(day)
                    }
                    val sortedDays = selectedDays
                        .map { EnumDayOfWeek.fromDisplayName(it) }
                        .sortedBy { it.order }
                        .map { it.displayName }
                    doctorWorkingDays = sortedDays
                    binding.days.setText(sortedDays.joinToString(", "))
                }
            }
        }

        checkBoxes.forEach { layout.addView(it) }

        AlertDialog.Builder(requireContext())
            .setTitle("Selecciona los dias de trabajo")
            .setView(layout)
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun showTimePicker(view: View, existingTime: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.custom_time_picker_dialog, null)

        val hourPicker = dialogView.findViewById<NumberPicker>(R.id.hour_picker).apply {
            minValue = 6
            maxValue = 22
        }

        val minutePicker = dialogView.findViewById<NumberPicker>(R.id.minute_picker).apply {
            minValue = 0
            maxValue = 1
            displayedValues = arrayOf("00", "30")
        }

        if (existingTime.isNotEmpty()) {
            val timeParts = existingTime.split(":")
            hourPicker.value = timeParts[0].toInt()
            minutePicker.value = if (timeParts[1] == "00") 0 else 1
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val hour = hourPicker.value
                val minute = if (minutePicker.value == 0) "00" else "30"

                when(view.id){
                    R.id.startTime -> binding.startTime.setText(String.format("%02d:%s", hour, minute))
                    R.id.endTime -> binding.endTime.setText(String.format("%02d:%s", hour, minute))
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setUpdateButtonClickListener() {
        binding.startTime.setOnClickListener {
            sharedViewModel.doctorSelected.value?.let { doctor ->
                showTimePicker(it, doctor.startTime ?: "")
            }
        }


        binding.endTime.setOnClickListener {
            sharedViewModel.doctorSelected.value?.let { doctor ->
                showTimePicker(it, doctor.startTime ?: "")
            }
        }

        binding.btnRegistrarDoctor.setOnClickListener {
            sharedViewModel.doctorSelected.value?.let { doctor ->
                updateDoctorFromInput(doctor)
            }

        }

        binding.btnCancel.setOnClickListener {
            navigateToAdminDoctorFragment()
        }

    }

    private fun updateDoctorFromInput(doctor: Doctor) {
        val name = binding.nameDoctor.text.toString()
        val lastName = binding.lastNameDoctor.text.toString()
        val startTime = binding.startTime.text.toString()
        val endTime = binding.endTime.text.toString()
        val workingDays = selectedDays.map { EnumDayOfWeek.fromDisplayName(it).name }
        val state = if (binding.enabledSwitch.isChecked) EnumDoctorStatus.ACTIVED else EnumDoctorStatus.RETIRED

        viewModel.updateDoctor(doctor.id.toString(),name, lastName, startTime, endTime, workingDays, state.toString())
    }

    private fun observeDoctorUpdate() {
        viewModel.updateResult.observe(viewLifecycleOwner, Observer { result ->
            handleUpdateResult(result)
        })
    }

    private fun handleUpdateResult(result: DoctorState) {
        when (result) {
            is DoctorState.Success -> navigateToAdminDoctorFragment()
            is DoctorState.Error -> showErrorSnackbar(result.exception)
        }
    }

    private fun showErrorSnackbar(exception: Exception) {
        view?.let {
            Snackbar.make(
                it,
                exception.localizedMessage ?: "Error al registrar",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun navigateToAdminDoctorFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_editDoctorFragment_to_adminDoctorsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}