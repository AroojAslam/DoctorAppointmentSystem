package com.example.doctorappointmentsystem

import DoctorTimingAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorProfile : Fragment(), DoctorTimingAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var timingAdapter: RecyclerView.Adapter<*>
    private lateinit var timingList: MutableList<DoctorTiming>
    private var selectedPosition = RecyclerView.NO_POSITION


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctor_profile, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""
        val backicon: ImageButton = view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_doctorProfile_to_homePage)
        }

        val bundle = arguments
        val doctorId = bundle?.getString("doctorId", "")
        val doctorName = bundle?.getString("doctorName", "")
        val doctorSpecialty = bundle?.getString("doctorSpecialty", "")
        val aboutDoctor = bundle?.getString("doctorAbout", "")


        val tvDoctorName: TextView = view.findViewById(R.id.nameTextView)
        val tvDoctorSpecialty: TextView = view.findViewById(R.id.specialtiesTextView)
        val tvDoctorAbout: TextView = view.findViewById(R.id.aboutTextView)

        tvDoctorName.text = doctorName
        tvDoctorSpecialty.text = doctorSpecialty
        tvDoctorAbout.text = aboutDoctor

        recyclerView = view.findViewById(R.id.recyclerViewDoctorTimings)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        timingList = mutableListOf()

        fetchAndDisplayDoctorTimings(doctorId.orEmpty())
        val button: Button = view.findViewById(R.id.button)
        button.setOnClickListener {
            if (selectedPosition != RecyclerView.NO_POSITION) {
                val selectedTiming = timingList[selectedPosition]
                navigateToAppointmentForm(selectedTiming,doctorName.orEmpty(),doctorSpecialty.orEmpty())
            } else {
                showToast("Please select any Slot")
            }
        }

        return view
    }

    private fun fetchAndDisplayDoctorTimings(doctorId: String) {
        val db = FirebaseFirestore.getInstance()
        val timingsCollection = db.collection("slots")

        timingsCollection.whereEqualTo("doctorUid", doctorId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    val hospitalName = document.getString("hospitalName")
                    val hours = document.getString("slotTiming")
                    val doctorUid = document.getString("doctorUid")
                    val timing = DoctorTiming(hospitalName.orEmpty(), hours.orEmpty(), doctorUid.orEmpty())
                    timingList.add(timing)
                }

                timingAdapter = DoctorTimingAdapter(timingList, requireContext(),this)

                recyclerView.adapter = timingAdapter
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToAppointmentForm(selectedTiming: DoctorTiming,doctorName: String,doctorSpecialty :String) {

        val bundle = bundleOf(
            "hospital" to selectedTiming.hospital,
            "hours" to selectedTiming.hours,
            "doctorName" to doctorName,
            "doctorSpecialty" to doctorSpecialty,
            "doctorUid" to selectedTiming.doctorUid
        )
        findNavController().navigate(R.id.action_doctorProfile_to_appointmentForm, bundle)

    }
    override fun onItemClick(position: Int) {

        selectedPosition = position
        timingAdapter.notifyDataSetChanged()
    }

}
