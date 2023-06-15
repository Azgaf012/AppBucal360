package com.dapm.appbucal360.utils


import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.util.Calendar


class DisableDaysDecorator(private val workingDays: List<Int>) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(day.year, day.month - 1, day.day)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return !workingDays.contains(dayOfWeek)
    }

    override fun decorate(view: DayViewFacade) {
        // Cambia el aspecto del día para que parezca deshabilitado.
        view.setDaysDisabled(true)
    }
}