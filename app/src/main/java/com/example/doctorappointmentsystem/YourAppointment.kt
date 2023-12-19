package com.example.doctorappointmentsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController

class YourAppointment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_your_appointment, container, false)
        val backicon :ImageButton =view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_yourAppointment_to_homePage)
        }
        return view
    }

}