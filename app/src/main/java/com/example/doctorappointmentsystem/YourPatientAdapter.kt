package com.example.doctorappointmentsystem

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class YourPatientAdapter(private var patients: List<Patient>) :
    RecyclerView.Adapter<YourPatientAdapter.YourPatientViewHolder>() {
    private val db = FirebaseFirestore.getInstance()

    class YourPatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientGender: TextView = itemView.findViewById(R.id.patientGender)
        val patientPhone: TextView = itemView.findViewById(R.id.patientPhone)
        val acceptButton: Button = itemView.findViewById(R.id.button3)
        val rejectButton: Button = itemView.findViewById(R.id.button4)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YourPatientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_your_patient, parent, false)
        return YourPatientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: YourPatientViewHolder, position: Int) {
        val patient = patients[position]

        holder.patientName.text = "${patient.name}"
        holder.patientGender.text = "${patient.gender}"
        holder.patientPhone.text = "${patient.phone}"

        holder.acceptButton.setOnClickListener {
            showAcceptConfirmationDialog(holder, patient)
        }

        holder.rejectButton.setOnClickListener {
            showRejectConfirmationDialog(holder, patient)
        }
    }


    override fun getItemCount(): Int {
        return patients.size
    }

    // Inside YourPatientAdapter

    private fun showAcceptConfirmationDialog(holder: YourPatientViewHolder, patient: Patient) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Accept Patient")
            .setMessage("Are you sure you want to accept ${patient.name}'s appointment?")
            .setPositiveButton("Accept") { _, _ ->

            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showRejectConfirmationDialog(holder: YourPatientViewHolder, patient: Patient) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Reject Patient")
            .setMessage("Are you sure you want to reject ${patient.name}'s appointment?")
            .setPositiveButton("Reject") { _, _ ->
                deletePatient(patient.patientId, holder.adapterPosition)

            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun deletePatient(patientId: String, position: Int) {
        val patientsCollection = db.collection("patients")
        patientsCollection.whereEqualTo("patientId", patientId).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    // Delete the patient document
                    patientsCollection.document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Log.d(TAG, "Patient data deleted successfully")
                            // Update local data after deletion
                            updateLocalData(position)
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error deleting patient data", e)
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error retrieving patient data for deletion", e)
            }
    }

    private fun updateLocalData(position: Int) {
        val mutablePatients = patients.toMutableList()
        mutablePatients.removeAt(position)
        patients = mutablePatients.toList()
        notifyDataSetChanged()
    }

}
