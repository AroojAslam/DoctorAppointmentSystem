package com.example.doctorappointmentsystem

import android.os.Bundle
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

        delay(2000)

        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {

            checkUserRole(currentUser.uid)
        } else {
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
                        findNavController().navigate(R.id.action_checkFragment_to_startPage)
                    }
                }
            } else {
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

                callback.invoke(null)
            }
    }

}