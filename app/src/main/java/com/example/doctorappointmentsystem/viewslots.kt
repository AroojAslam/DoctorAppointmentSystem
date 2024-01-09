package com.example.doctorappointmentsystem

import SlotsAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class viewslots : Fragment(), SlotsAdapter.OnDeleteClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var slotsAdapter: SlotsAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var currentUserId: String? = null // Declare currentUserId as a class variable

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_viewslots, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""
        val backicon: ImageButton = view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_viewslots_to_docProfile2)
        }

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUserId = auth.currentUser?.uid // Assign the currentUserId here

        // Set up RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        slotsAdapter = SlotsAdapter(this)
        recyclerView.adapter = slotsAdapter

        // Fetch and display slot data
        fetchSlotData(currentUserId, view)

        return view
    }

    private fun fetchSlotData(doctorUid: String?, view: View) {
        if (doctorUid != null) {
            val slotsCollection = firestore.collection("slots")

            slotsCollection.whereEqualTo("doctorUid", doctorUid)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val slotsList = mutableListOf<Slot>()

                    for (document in querySnapshot) {
                        val id = document.id
                        val hospitalName = document.getString("hospitalName")
                        val slotTiming = document.getString("slotTiming")

                        val slot = Slot(id, hospitalName, slotTiming)
                        slotsList.add(slot)
                    }

                    slotsAdapter.submitList(slotsList)
                }
                .addOnFailureListener { exception ->
                    // Handle failure
                }
        }
    }

    override fun onDeleteClick(slot: Slot) {
        showDeleteConfirmationDialog(slot)
    }

    private fun showDeleteConfirmationDialog(slot: Slot) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Delete Slot")
            .setMessage("Are you sure you want to delete this slot?")
            .setPositiveButton("Yes") { _, _ ->
                deleteSlot(slot)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteSlot(slot: Slot) {
        val slotsCollection = FirebaseFirestore.getInstance().collection("slots")

        slotsCollection.document(slot.id.toString())
            .delete()
            .addOnSuccessListener {

                Toast.makeText(requireContext(), "Slot deleted successfully", Toast.LENGTH_SHORT).show()

                fetchSlotData(currentUserId, requireView())
            }
            .addOnFailureListener { e ->

                Toast.makeText(requireContext(), "Error deleting slot", Toast.LENGTH_SHORT).show()
            }
    }
}
