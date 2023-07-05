package com.dapm.appbucal360.utils

import java.util.*

enum class EnumDayOfWeek(val calendarDay: Int, val displayName: String, val order: Int)  {
    MONDAY(Calendar.MONDAY, "Lunes", 1),
    TUESDAY(Calendar.TUESDAY, "Martes", 2),
    WEDNESDAY(Calendar.WEDNESDAY, "Miércoles", 3),
    THURSDAY(Calendar.THURSDAY, "Jueves", 4),
    FRIDAY(Calendar.FRIDAY, "Viernes", 5),
    SATURDAY(Calendar.SATURDAY, "Sábado", 6),
    SUNDAY(Calendar.SUNDAY, "Domingo", 7);

    companion object {
        fun fromDisplayName(displayName: String): EnumDayOfWeek {
            return values().find { it.displayName == displayName }
                ?: throw IllegalArgumentException("No enum constant ${EnumDayOfWeek::class.java.name}.$displayName")
        }
    }
}