package com.example.doctorappointmentsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

class StartPage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view =inflater.inflate(R.layout.fragment_start_page, container, false)
        val button : Button = view.findViewById(R.id.button)
        button.setOnClickListener{
            findNavController().navigate(R.id.action_startPage_to_homePage)
        }
        return view
    }


}