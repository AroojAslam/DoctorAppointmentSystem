package com.example.doctorappointmentsystem

data class Slot(
    val id: String? = null, // You may want to use a more appropriate type for id, like Int
    val hospitalName: String? = null,
    val slotTiming: String? = null
)
