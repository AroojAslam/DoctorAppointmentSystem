package com.example.doctorappointmentsystem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DoctorSignup : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doctor_signup, container, false)
        val logInText: TextView = view.findViewById(R.id.logInText)
        logInText.setOnClickListener {
            findNavController().navigate(R.id.action_doctorSignup_to_login)
        }

        val signupButton: Button = view.findViewById(R.id.signupButton)
        signupButton.setOnClickListener {
            signupDoctor()
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val specialtySpinner = view.findViewById<Spinner>(R.id.doctorSignupSpecialtySpinner)
        val specialtyTextInputLayout = view.findViewById<TextInputLayout>(R.id.specialtyTextInputLayout)

        val specialtyOptions = arrayOf("Cardiologist", "Dentist", "Orthopedic Surgeon")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, specialtyOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        specialtySpinner.adapter = adapter

        specialtySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedSpecialty = specialtyOptions[position]
                // Ensure that the selected specialty is being captured correctly
                specialtyTextInputLayout.error = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected if needed
            }
        }
    }

    private fun signupDoctor() {
        val emailEditText = view?.findViewById<EditText>(R.id.signupEmailEditText)
        val passwordEditText = view?.findViewById<EditText>(R.id.signupPasswordEditText)
        val nameEditText = view?.findViewById<EditText>(R.id.signupUsernameEditText)
        val aboutEditText = view?.findViewById<EditText>(R.id.doctorSignupAboutEditText)

        val email = emailEditText?.text.toString()
        val password = passwordEditText?.text.toString()
        val name = nameEditText?.text.toString()
        val specialtySpinner = view?.findViewById<Spinner>(R.id.doctorSignupSpecialtySpinner)
        val specialty = specialtySpinner?.selectedItem.toString()
        val about = aboutEditText?.text.toString()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || specialty.isEmpty() || about.isEmpty()) {
            // Handle empty fields
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        saveUserDetailsToFirestore(it.uid, "doctor")
                    }
                    // Save doctor details to Firestore
                    user?.let {
                        saveDoctorDetailsToFirestore(it.uid, name, specialty, about)
                    }
                    // Handle successful signup
                    Toast.makeText(requireContext(), "Account created successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_doctorSignup_to_login)
                } else {
                    // Handle signup failure
                    val errorMessage = task.exception?.message ?: "Authentication failed."
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun saveDoctorDetailsToFirestore(uid: String, name: String, specialty: String, about: String) {
        val doctorMap = hashMapOf(
            "uid" to uid,
            "name" to name,
            "specialty" to specialty,
            "about" to about
            // Add more doctor details as needed
        )

        firestore.collection("doctors").document(uid)
            .set(doctorMap)
            .addOnSuccessListener {
                // Successfully saved doctor details to Firestore
            }
            .addOnFailureListener { e ->
                // Handle failure to save doctor details
                Toast.makeText(
                    requireContext(),
                    "Failed to save doctor details: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
