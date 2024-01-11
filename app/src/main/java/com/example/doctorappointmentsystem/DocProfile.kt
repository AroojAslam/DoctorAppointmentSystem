package com.example.doctorappointmentsystem

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DocProfile : Fragment() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var nameTextView: TextView
    private lateinit var specialtiesTextView: TextView
    private lateinit var aboutTextView: TextView
    private lateinit var docTimingEditText: TextView
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var startTime = Calendar.getInstance()
    private var endTime = Calendar.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_doc_profile, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""
        val backicon: ImageButton = view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_docProfile2_to_doctorHome2)
        }
        val currentUserId = firebaseAuth.currentUser?.uid
        val currentUserEmail=firebaseAuth.currentUser?.email
        val emailTextView:TextView=view.findViewById(R.id.emailTextView)
        emailTextView.text=currentUserEmail
        if (currentUserId != null) {
            fetchDoctorData(currentUserId, view)
        }

        val addSlotButton: Button = view.findViewById(R.id.addSlotButton)
        addSlotButton.setOnClickListener {
            showHospitalSelectionPopup()
        }
        val viewSlotButton :Button =view.findViewById(R.id.ViewSlotButton)
        viewSlotButton.setOnClickListener {
            findNavController().navigate(R.id.action_docProfile2_to_viewslots)
        }
        val editProfileButton: Button = view.findViewById(R.id.editProfileButton)
        editProfileButton.setOnClickListener {
            showEditProfileDialog()
        }
        return view
    }

    private fun fetchDoctorData(uid: String, view: View) {
        val doctorsCollection = firestore.collection("doctors")

        doctorsCollection.whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                for (document: QueryDocumentSnapshot in querySnapshot) {

                    val name = document.getString("name")
                    val specialties = document.getString("specialty")
                    val about = document.getString("about")


                     nameTextView = view.findViewById(R.id.nameTextView)
                     specialtiesTextView = view.findViewById(R.id.specialtiesTextView)
                     aboutTextView = view.findViewById(R.id.aboutTextView)

                    nameTextView.text = name
                    specialtiesTextView.text = specialties
                    aboutTextView.text = about

                }
            }
            .addOnFailureListener { exception ->

            }
    }

    private fun showHospitalSelectionPopup() {
        val hospitalsCollection = firestore.collection("hospital")

        val hospitalsList = mutableListOf<String>()

        hospitalsCollection.get()
            .addOnSuccessListener { querySnapshot: QuerySnapshot ->
                for (document: QueryDocumentSnapshot in querySnapshot) {
                    val hospitalName = document.getString("name")
                    hospitalName?.let {
                        hospitalsList.add(it)
                    }
                }

                // Show a dialog with hospital options
                showHospitalDialog(hospitalsList)
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
    fun showTimePickerDialog(view: View) {
        val is24HourFormat = false
        val timePickerDialog = TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->

                if (view.id == R.id.docTiming) {
                    startTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    startTime.set(Calendar.MINUTE, minute)

                    endTime.timeInMillis = startTime.timeInMillis + 15 * 60 * 1000
                } else {

                }
                updateEditText()
            },
            startTime.get(Calendar.HOUR_OF_DAY),
            startTime.get(Calendar.MINUTE),
            is24HourFormat
        )

        timePickerDialog.show()
    }


    private fun updateEditText() {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val startTimeText = dateFormat.format(startTime.time)
        val endTimeText = dateFormat.format(endTime.time)
        val timeRangeText = "$startTimeText - $endTimeText"
        docTimingEditText.setText(timeRangeText)
    }
    @SuppressLint("MissingInflatedId")
    private fun showHospitalDialog(hospitalsList: List<String>) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Add Slot")

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_add_slot, null)

        val hospitalSpinner: Spinner = dialogLayout.findViewById(R.id.hospitalSpinner)
        docTimingEditText = dialogLayout.findViewById(R.id.docTiming)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hospitalsList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hospitalSpinner.adapter = adapter

        docTimingEditText.setOnClickListener {
            showTimePickerDialog(docTimingEditText)
        }

        builder.setView(dialogLayout)

        builder.setPositiveButton("OK") { _, _ ->
            val selectedHospital = hospitalSpinner.selectedItem.toString()
            val slotTimingEditText: EditText = dialogLayout.findViewById(R.id.docTiming)
            val slotTiming = slotTimingEditText.text.toString().trim()

            if (slotTiming.isNotEmpty()) {
                val currentUserId = firebaseAuth.currentUser?.uid
                if (currentUserId != null) {

                    val slotsCollection = firestore.collection("slots")
                    val timestamp = System.currentTimeMillis()
                    val newSlot = hashMapOf(
                        "id" to timestamp.toString(),
                        "doctorUid" to currentUserId,
                        "hospitalName" to selectedHospital,
                        "slotTiming" to slotTiming
                    )

                    // Add the new slot document to the Firestore collection
                    slotsCollection.add(newSlot)
                        .addOnSuccessListener { documentReference ->
                            // Slot added successfully
                            Toast.makeText(requireContext(), "Slot added successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            // Error adding slot
                            Toast.makeText(requireContext(), "Error adding slot", Toast.LENGTH_SHORT).show()
                        }
                }
            } else {
                Toast.makeText(requireContext(), "Slot Timing cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }


        builder.setNegativeButton("Cancel") { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }
    private fun showEditProfileDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Edit Profile")

        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_edit_profile, null)

        val nameEditText: EditText = dialogLayout.findViewById(R.id.editName)
        val specialtiesEditText: EditText = dialogLayout.findViewById(R.id.editSpecialties)
        val aboutEditText: EditText = dialogLayout.findViewById(R.id.editAbout)

        nameEditText.setText(nameTextView.text)
        specialtiesEditText.setText(specialtiesTextView.text)
        aboutEditText.setText(aboutTextView.text)

        builder.setView(dialogLayout)

        builder.setPositiveButton("Save") { _, _ ->
            val editedName = nameEditText.text.toString().trim()
            val editedSpecialties = specialtiesEditText.text.toString().trim()
            val editedAbout = aboutEditText.text.toString().trim()
            nameTextView.text = editedName
            specialtiesTextView.text = editedSpecialties
            aboutTextView.text = editedAbout


            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Cancel") { _, _ -> }

        val dialog = builder.create()
        dialog.show()
    }

}

