package com.dapm.appbucal360.utils

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.dapm.appbucal360.model.doctor.Doctor
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.LocalTime
import java.util.Calendar

abstract class CalendarViewAppointmentFragment : Fragment(){
    protected var calendarView: MaterialCalendarView? = null
    protected var timeAutocomplete: AutoCompleteTextView? = null

    protected fun updateCalendarView(doctor: Doctor) {
        val workingDays = doctor.workingDays?.let { convertDayNamesToCalendarDays(it) }
        calendarView?.addDecorators(workingDays?.let { DisableDaysDecorator(it) })
    }

    protected fun updateTimeAutocomplete(doctor: Doctor) {
        val availableTimes = generateAvailableTimes(doctor!!.startTime!!, doctor!!.endTime!!)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            availableTimes
        )
        timeAutocomplete?.setAdapter(adapter)
    }

    protected fun generateAvailableTimes(start: String, end: String): List<String> {
        val availableTimes = mutableListOf<String>()
        var startTime = LocalTime.parse(start)
        var endTime = LocalTime.parse(end)

        while (startTime.isBefore(endTime) || startTime == endTime) {
            availableTimes.add(startTime.toString())
            startTime = startTime.plusMinutes(30)
        }

        return availableTimes
    }

    protected fun convertDayNamesToCalendarDays(dayNames: List<String>): List<Int> {
        return dayNames.map { dayName ->
            when (dayName) {
                "Monday" -> Calendar.MONDAY
                "Tuesday" -> Calendar.TUESDAY
                "Wednesday" -> Calendar.WEDNESDAY
                "Thursday" -> Calendar.THURSDAY
                "Friday" -> Calendar.FRIDAY
                "Saturday" -> Calendar.SATURDAY
                "Sunday" -> Calendar.SUNDAY
                else -> throw IllegalArgumentException("Invalid day name: $dayName")
            }
        }
    }
}