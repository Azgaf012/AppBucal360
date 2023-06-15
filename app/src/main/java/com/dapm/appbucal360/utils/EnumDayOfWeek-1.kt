package com.dapm.appbucal360.utils

import java.util.Calendar
import java.util.Locale

enum class DayOfWeek(val calendarDay: Int) {
    MONDAY(Calendar.MONDAY),
    TUESDAY(Calendar.TUESDAY),
    WEDNESDAY(Calendar.WEDNESDAY),
    THURSDAY(Calendar.THURSDAY),
    FRIDAY(Calendar.FRIDAY),
    SATURDAY(Calendar.SATURDAY),
    SUNDAY(Calendar.SUNDAY);

    companion object {
        fun fromString(dayName: String): DayOfWeek {
            return valueOf(dayName.uppercase(Locale.ROOT))
        }
    }
}