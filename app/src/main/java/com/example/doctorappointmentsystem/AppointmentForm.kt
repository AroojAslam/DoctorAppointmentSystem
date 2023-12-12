package com.example.doctorappointmentsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class AppointmentForm : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_appointment_form, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Appointment Form"
        val actionBar: ActionBar? = (activity as AppCompatActivity?)?.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
      val submit : Button =view.findViewById(R.id.submit)
     submit.setOnClickListener{
         Toast.makeText(requireContext(), "Your Appointment is Added", Toast.LENGTH_SHORT).show()
         findNavController().navigate(R.id.action_appointmentForm_to_homePage)
     }
        return view
    }

}