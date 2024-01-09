package com.example.doctorappointmentsystem

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
import com.google.firebase.auth.FirebaseAuth

class DoctorHome : Fragment() {
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

}