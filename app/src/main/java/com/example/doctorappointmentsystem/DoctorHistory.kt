package com.example.doctorappointmentsystem

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorHistory : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConfirmPatientAdapter
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userUid = currentUser?.uid

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctor_history, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""
        val backicon: ImageButton = view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_doctorHistory_to_doctorHome2)
        }
        recyclerView = view.findViewById(R.id.recyclerView)
        adapter = ConfirmPatientAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        fetchConfirmPatientsFromFirestore()
        return view
    }

    private fun fetchConfirmPatientsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("confirm_patients")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val confirmPatients = mutableListOf<ConfirmPatient>()
                    for (document in task.result!!) {
                        val id = document.getString("id") ?: ""
                        val doctorUid = document.getString("doctorUid") ?: ""
                        val patientUid = document.getString("patientUid") ?: ""
                        val name = document.getString("name") ?: ""
                        val gender = document.getString("gender") ?: ""
                        val phone = document.getString("phone") ?: ""
                        val hospital = document.getString("hospital") ?: ""
                        val hours = document.getString("hours") ?: ""

                        if (doctorUid == userUid) {
                            confirmPatients.add(ConfirmPatient(id, doctorUid, patientUid, name, gender, phone, hospital, hours))
                        }
                    }

                    adapter.submitList(confirmPatients)
                } else {
                    // Handle errors
                }
            }
    }

}

