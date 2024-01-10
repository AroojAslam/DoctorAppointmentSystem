package com.example.doctorappointmentsystem

import PatientAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class YourAppointment : Fragment() {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userUid = currentUser?.uid
    private lateinit var patientAdapter: PatientAdapter
    private val patientList = mutableListOf<Patient>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =inflater.inflate(R.layout.fragment_your_appointment, container, false)
        val backicon :ImageButton =view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_yourAppointment_to_homePage)
        }
        val recyclerView:RecyclerView=view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        patientAdapter=PatientAdapter(patientList)
        recyclerView.adapter=patientAdapter
        fetchPatientsFromFirestore()


        return view
    }
    private fun fetchPatientsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("patients")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val patients = mutableListOf<Patient>()
                    for (document in task.result!!) {
                        val uid = document.getString("uid") ?: ""
                        val doctorName = document.getString("doctorName") ?: ""
                        val doctorSpecialty = document.getString("doctorSpecialty") ?: ""
                        val name = document.getString("name") ?: ""
                        val gender = document.getString("gender") ?: ""
                        val phone = document.getString("phone") ?: ""
                        val hospital = document.getString("hospital") ?: ""
                        val hours = document.getString("hours") ?: ""
                        val patientId = document.getString("patientId") ?: ""
          if(uid==userUid){
                         patients.add(Patient(uid, doctorName, doctorSpecialty, name, gender, phone, hospital,hours,patientId))
                    }

                    }

                    patientList.addAll(patients)
                    patientAdapter.notifyDataSetChanged()
                } else {
                    // Handle errors
                }
            }
    }


}