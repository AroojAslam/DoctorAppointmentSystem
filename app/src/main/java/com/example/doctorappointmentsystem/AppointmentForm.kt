package com.example.doctorappointmentsystem
import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppointmentForm : Fragment() {
    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var genderEditText: EditText
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_appointment_form, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""

        val bundle = arguments
        val hospitalName = bundle?.getString("hospital", "")
        val hours = bundle?.getString("hours", "")
        val docName = bundle?.getString("doctorName", "")
        val docSpecialty = bundle?.getString("doctorSpecialty", "")
        val doctorUid = bundle?.getString("doctorUid", "")
      val doctorSpecialty :TextView=view.findViewById(R.id.DoctorSpecialty)
        doctorSpecialty.text="($docSpecialty)"
     val doctorName :TextView =view.findViewById(R.id.doctorName)
         doctorName.text=docName
        val timing: TextView = view.findViewById(R.id.timing)
        timing.text = hours
       val hospitalNameTextView: TextView = view.findViewById(R.id.hospitalName)
    hospitalNameTextView.text=hospitalName

        val backicon: ImageButton = view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_appointmentForm_to_homePage)
        }
        nameEditText = view.findViewById(R.id.nameEditText)
        phoneEditText = view.findViewById(R.id.phoneEditText)
        genderEditText = view.findViewById(R.id.genderEditText)
        val submit: Button = view.findViewById(R.id.submit)
        submit.setOnClickListener {
            val name = nameEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val gender = genderEditText.text.toString()
      if(name.isEmpty() || gender.isEmpty() || phone.isEmpty() ){
    Toast.makeText(requireContext(), "Enter Your Data", Toast.LENGTH_SHORT).show()
     }else if(validateInput()){

    addPatientData(name, phone, gender,docName.orEmpty(),docSpecialty.orEmpty(),hours.orEmpty(),hospitalName.orEmpty(),doctorUid.toString())
    Toast.makeText(requireContext(), "Your Appointment is Added", Toast.LENGTH_SHORT).show()
    findNavController().navigate(R.id.action_appointmentForm_to_homePage)
     }else {

    }

        }

        return view
    }

    private fun addPatientData(
        name: String,
        phone: String,
        gender: String,
        docName: String,
        docSpecialty: String,
        hours: String,
        hospital: String,
        doctorUid:String,
    ) {

        val db = FirebaseFirestore.getInstance()
        if (currentUser != null) {
            val currentUserId = currentUser.uid

            val patientId = generateUniquePatientId(name)

            val patient = hashMapOf(
                "uid" to currentUserId,
                "patientId" to patientId,
                "name" to name,
                "phone" to phone,
                "gender" to gender,
                "doctorName" to docName,
                "doctorSpecialty" to docSpecialty,
                "hours" to hours,
                "hospital" to hospital,
                "doctorUid" to doctorUid,
            )

            db.collection("patients")
                .add(patient)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Patient data added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding patient data", e)
                }
        }
    }

    private fun generateUniquePatientId(name: String): String {
        val timestamp = System.currentTimeMillis()
        val randomPart = (0..9999).random() // You can adjust the range as needed
        return "$name-$timestamp-$randomPart"
    }

    private fun validateInput(): Boolean {
        val name = nameEditText.text.toString().trim()
        val phone = phoneEditText.text.toString().trim()
        val gender = genderEditText.text.toString().trim()

        if (TextUtils.isEmpty(name) || !name.matches("[a-zA-Z ]+".toRegex())) {
            nameEditText.error = "Enter a valid name with only alphabets"
            return false
        }


        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || phone.length != 11) {
            phoneEditText.error = "Enter a valid 11-digit phone number"
            return false
        }


        if (TextUtils.isEmpty(gender) || (!gender.equals("male", ignoreCase = true) && !gender.equals(
                "female",
                ignoreCase = true
            ) && !gender.equals("other", ignoreCase = true))
        ) {
            genderEditText.error = "Enter a valid gender (male, female, or other)"
            return false
        }

        return true
    }

}
