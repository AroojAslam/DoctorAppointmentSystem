package com.example.doctorappointmentsystem
import CategorieAdapter
import DoctorAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomePage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_home_page, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = "Doctor Appointment"
        val doctorList = listOf(
            Doctor("Dr. Smith", "Cardiologist"),
            Doctor("Dr. Johnson", "Dermatologist"),
            Doctor("Dr. Brown", "Pediatrician"),
            Doctor("Dr.Brown", "Pediatrician"),
            Doctor("Dr. Brown", "Pediatrician"),
            Doctor("Dr. Brown", "Pediatrician")
        )
        val DocType = listOf("All", "Cardiologist", "Dentist","Orthopedic Surgeon","Neurologist")
        val icons = listOf("@drawable/logo", "@drawable/ic_heart", "@drawable/ic_tooth","@drawable/ic_bone","@drawable/ic_brain")
        val recyclerView: RecyclerView = view.findViewById(R.id.horizontalRecyclerView)
        val doctorRecyclerView: RecyclerView = view.findViewById(R.id.verticalRecyclerView)
        val doctorLayoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val adapter = CategorieAdapter(DocType,icons)
        recyclerView.adapter = adapter
        doctorRecyclerView.layoutManager = doctorLayoutManager

        val doctorAdapter = DoctorAdapter(doctorList)
        doctorRecyclerView.adapter = doctorAdapter

        doctorAdapter.setOnItemClickListener {
            findNavController().navigate(R.id.action_homePage_to_doctorProfile)
        }
        return view

    }



}