package com.example.doctorappointmentsystem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserSignup : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_signup, container, false)
        val signupButton: Button = view.findViewById(R.id.signupButton)
        val emailEditText: EditText = view.findViewById(R.id.signupEmailEditText)
        val passwordEditText: EditText = view.findViewById(R.id.signupPasswordEditText)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar3)

        signupButton.setOnClickListener {
            signupButton.text = " "
            progressBar.visibility = View.VISIBLE
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter username, email, and password",
                    Toast.LENGTH_SHORT
                ).show()
                signupButton.text = "SignUp"
                progressBar.visibility = View.GONE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        user?.let {
                            saveUserDetailsToFirestore(it.uid, "user")
                        }

                        Toast.makeText(
                            requireContext(),
                            "Account created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        signupButton.text = "SignUp"
                        progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_signup_to_homePage)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        signupButton.text = "SignUp"
                        progressBar.visibility = View.GONE
                    }
                }
        }

        val loginText: TextView = view.findViewById(R.id.logInText)
        loginText.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }

        return view
    }

    private fun saveUserDetailsToFirestore(uid: String, role: String) {
        val userMap = hashMapOf(
            "uid" to uid,
            "role" to role

        )

        firestore.collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

                Toast.makeText(
                    requireContext(),
                    "Failed to save user details: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}
