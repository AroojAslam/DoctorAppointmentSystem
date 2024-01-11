package com.example.doctorappointmentsystem

data class ConfirmPatient(
    val id: String,
    val doctorUid: String,
    val patientUid: String,
    val name: String,
    val gender: String,
    val phone: String,
    val hospital: String,
    val hours:String,

)
