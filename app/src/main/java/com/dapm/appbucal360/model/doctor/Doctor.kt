package com.dapm.appbucal360.model.doctor

data class Doctor(
    val id: String? = null,
    val name: String? = null,
    val lastName: String? = null,
    val workingDays: List<String> = emptyList(),
    val startTime: String? = null,
    val endTime: String? = null,
    var state: String? = null
){

    constructor() : this(null,null,null,emptyList(),null,null, null)
    override fun toString(): String {
        return "$name $lastName"
    }
}
