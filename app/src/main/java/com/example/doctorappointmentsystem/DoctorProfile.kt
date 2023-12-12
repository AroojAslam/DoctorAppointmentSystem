package com.example.doctorappointmentsystem
import TimeSlotAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController

class DoctorProfile : Fragment() {
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_doctor_profile, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Doctor Profile"
        val gridView: GridView = view.findViewById(R.id.gridView)
        val timeSlots = listOf(
            "10:00 AM", "10:30 AM", "11:00 AM",
            "11:30 AM", "12:00 PM", "12:30 PM",

        )
        val button : Button=view.findViewById(R.id.button)
        button.setOnClickListener{
            findNavController().navigate(R.id.action_doctorProfile_to_appointmentForm)
        }
        val adapter = TimeSlotAdapter(requireContext(), timeSlots)
        gridView.adapter = adapter

        return view
    }

}