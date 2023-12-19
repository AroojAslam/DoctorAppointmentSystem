package com.example.doctorappointmentsystem
import CategorieAdapter
import DoctorAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class HomePage : Fragment() {
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

        val DocType = listOf("All", "Cardiologist", "Dentist", "Orthopedic Surgeon", "Neurologist")
        val icons = listOf(
            "@drawable/logo",
            "@drawable/ic_heart",
            "@drawable/ic_tooth",
            "@drawable/ic_bone",
            "@drawable/ic_brain"
        )
        val recyclerView: RecyclerView = view.findViewById(R.id.horizontalRecyclerView)
        val doctorRecyclerView: RecyclerView = view.findViewById(R.id.verticalRecyclerView)
        val doctorLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val adapter = CategorieAdapter(DocType, icons)
        recyclerView.adapter = adapter
        doctorRecyclerView.layoutManager = doctorLayoutManager


        val appButton: Button = view.findViewById(R.id.button2)
        appButton.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_yourAppointment)
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.verticalRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = DoctorAdapter(doctorList)
        recyclerView.adapter = adapter

        fetchDataFromFirestore()
    }
    private fun fetchDataFromFirestore() {
        firestore.collection("doctor")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val name = document.getString("name")
                    val specialties = document.getString("specialties")
                    val about = document.getString("about")
                    val id = document.getString("id")

                    val doctor = Doctor(name, specialties, about, id)
                    doctorList.add(doctor)
                }

                // Notify the adapter that the data set has changed
                recyclerView.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                // Handle errors
                println("Error getting documents: $exception")
            }
    }
}