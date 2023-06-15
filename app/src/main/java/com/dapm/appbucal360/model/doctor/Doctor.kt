package com.dapm.appbucal360.model.doctor

import java.time.LocalTime
import java.util.UUID

data class Doctor(
    val id: UUID,
    val name: String,
    val lastName: String,
    val workingDays: List<String>,
    val startTime: LocalTime,
    val endTime: LocalTime
){
    override fun toString(): String {
        return "$name $lastName"
    }
}
