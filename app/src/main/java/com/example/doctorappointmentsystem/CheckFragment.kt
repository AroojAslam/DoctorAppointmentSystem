package com.example.doctorappointmentsystem

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CheckFragment : Fragment() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_check, container, false)
        progressBar = view.findViewById(R.id.progressBar)


        GlobalScope.launch(Dispatchers.Main) {
            simulateBackgroundTask()
        }

        return view
    }
    private suspend fun simulateBackgroundTask() {
        // Simulate a delay of 3 seconds (adjust as needed)
        delay(2000)

        // Check if the user is already logged in
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            // User is already logged in
            // Check user role
            checkUserRole(currentUser.uid)
        } else {
            // User is not logged in, navigate to the login screen
            findNavController().navigate(R.id.action_checkFragment_to_startPage)
        }
    }

    private fun checkUserRole(uid: String) {

        getRoleFromFirestore(uid) { role ->
            if (role != null) {

                when (role) {
                    "doctor" -> findNavController().navigate(R.id.action_checkFragment_to_doctorHome2)
                    "user" -> findNavController().navigate(R.id.action_checkFragment_to_homePage)
                    else -> {
                        // Handle unknown role
                        findNavController().navigate(R.id.action_checkFragment_to_startPage)
                    }
                }
            } else {
                // Role not available or error, navigate to the start page
                findNavController().navigate(R.id.action_checkFragment_to_startPage)
            }
        }
    }

    private fun getRoleFromFirestore(uid: String, callback: (String?) -> Unit) {
        firestore.collection("users").document(uid)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                val role = documentSnapshot.getString("role")
                callback.invoke(role)
            }
            .addOnFailureListener { e ->
                // Handle failure to retrieve user information
                callback.invoke(null)
            }
    }

}