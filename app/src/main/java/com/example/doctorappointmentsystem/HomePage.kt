package com.example.doctorappointmentsystem
import CategoryAdapter
import DoctorAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HomePage : Fragment() {
    private lateinit var doctorAdapter: DoctorAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var logoutButton: ImageButton
    private lateinit var auth: FirebaseAuth
    private val doctorList = mutableListOf<Doctor>()
    private lateinit var recyclerView: RecyclerView
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)
        auth = FirebaseAuth.getInstance()
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = " "

        // Initialize CategoryAdapter
        val DocType = listOf("All", "Cardiologist", "Dentist", "Orthopedic Surgeon", "Neurologist")
        val icons = listOf(
            "@drawable/logo",
            "@drawable/ic_heart",
            "@drawable/ic_tooth",
            "@drawable/ic_bone",
            "@drawable/ic_brain"
        )

        val recyclerView1: RecyclerView = view.findViewById(R.id.horizontalRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView1.layoutManager = layoutManager
        categoryAdapter = CategoryAdapter(DocType, icons) { selectedCategory ->
            doctorAdapter.updateDoctorList(doctorList, selectedCategory)
        }
        recyclerView1.adapter = categoryAdapter

        recyclerView = view.findViewById(R.id.verticalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        doctorAdapter = DoctorAdapter(doctorList)
        recyclerView.adapter = doctorAdapter

        // Fetch data from Firestore
        fetchDoctorsFromFirestore()

        val appButton: Button = view.findViewById(R.id.button2)
        appButton.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_yourAppointment)
        }

        logoutButton = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        return view
    }

    private fun fetchDoctorsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("doctors")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val doctors = mutableListOf<Doctor>()
                    for (document in task.result!!) {
                        val name = document.getString("name") ?: ""
                        val doctorId = document.getString("uid") ?: ""
                        val specialty = document.getString("specialty") ?: ""
                        val about = document.getString("about") ?: ""
                        doctors.add(Doctor(name, specialty, about, doctorId))
                    }
                    doctorList.clear()
                    doctorList.addAll(doctors)
                    doctorAdapter.notifyDataSetChanged()
                } else {
                    // Handle errors
                }
            }
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
        findNavController().navigate(R.id.action_homePage_to_login)
    }
}
