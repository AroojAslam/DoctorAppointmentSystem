package com.example.doctorappointmentsystem

import android.annotation.SuppressLint
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

class login : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val  auth = FirebaseAuth.getInstance()
val view =inflater.inflate(R.layout.fragment_login, container, false)
 val loginButton : Button = view.findViewById(R.id.loginButton)
        val loginProgressBar : ProgressBar=view.findViewById(R.id.loginProgressBar)
        val emailEditText: EditText = view.findViewById(R.id.signupEmailEditText)
        val passwordEditText: EditText = view.findViewById(R.id.passwordEditText)
        loginButton.setOnClickListener {
            loginButton.text=""
            loginProgressBar.visibility=View.VISIBLE
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
                loginButton.text="Login"
                loginProgressBar.visibility=View.GONE
                return@setOnClickListener
            }

            // Firebase authentication
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        val user = auth.currentUser
                        Toast.makeText(
                            requireContext(),
                            "Login successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        loginButton.text="Login"
                        loginProgressBar.visibility=View.GONE

                        findNavController().navigate(R.id.action_login_to_homePage)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Authentication failed. Check your email and password.",
                            Toast.LENGTH_SHORT
                        ).show()
                        loginButton.text="Login"
                        loginProgressBar.visibility=View.GONE
                    }
                }
        }

        val signupText: TextView = view.findViewById(R.id.signuptext)
        signupText.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        return view
    }

}