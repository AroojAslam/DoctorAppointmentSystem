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
import com.google.firebase.auth.FirebaseUser

class signup : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val  auth = FirebaseAuth.getInstance()
       val view = inflater.inflate(R.layout.fragment_signup, container, false)
        val signupButton: Button =view.findViewById(R.id.signupButton)
        val emailEditText: EditText = view.findViewById(R.id.signupEmailEditText)
        val passwordEditText: EditText = view.findViewById(R.id.signupPasswordEditText)
        val progressBar :ProgressBar =view.findViewById(R.id.progressBar3)

        signupButton.setOnClickListener {
            signupButton.text=" "
            progressBar.visibility=View.VISIBLE
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {

                Toast.makeText(
                    requireContext(),
                    "Please enter username,email and password",
                    Toast.LENGTH_SHORT
                ).show()
                signupButton.text="SignUp"
                progressBar.visibility=View.GONE
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign up success
                        val user = auth.currentUser
                        Toast.makeText(
                            requireContext(),
                            "Account created successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        signupButton.text="SignUp"
                        progressBar.visibility=View.GONE
                        findNavController().navigate(R.id.action_signup_to_login)
                    } else {

                        Toast.makeText(
                            requireContext(),
                            "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                        signupButton.text="SignUp"
                        progressBar.visibility=View.GONE
                    }
                }
        }

        val loginText: TextView = view.findViewById(R.id.logInText)
        loginText.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }

        return view
    }


}