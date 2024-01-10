package com.example.doctorappointmentsystem

import PatientAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorHome : Fragment() {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val userUid = currentUser?.uid
    private lateinit var yourPatientAdapter: YourPatientAdapter
    private val patientList = mutableListOf<Patient>()
    private lateinit var logoutButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view=inflater.inflate(R.layout.fragment_doctor_home, container, false)
        logoutButton = view.findViewById(R.id.logoutButton)
        val docProfile:LinearLayout =view.findViewById(R.id.docProfile)
        docProfile.setOnClickListener {
      findNavController().navigate(R.id.action_doctorHome2_to_docProfile2)
        }
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }
        val recyclerView: RecyclerView =view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        yourPatientAdapter=YourPatientAdapter(patientList)
        recyclerView.adapter=yourPatientAdapter
        fetchPatientsFromFirestore()

        return view
    }
    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                logoutUser()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun logoutUser() {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        findNavController().navigate(R.id.action_doctorHome2_to_login)
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
                        val doctorUid = document.getString("doctorUid") ?: ""
                        if(doctorUid==userUid){
                            patients.add(Patient(uid, doctorName, doctorSpecialty, name, gender, phone, hospital,hours,patientId))
                        }

                    }

                    patientList.addAll(patients)
                    yourPatientAdapter.notifyDataSetChanged()
                } else {
                    // Handle errors
                }
            }
    }

}