
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.Patient
import com.example.doctorappointmentsystem.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class  YourPatientAdapter(private var patients: List<Patient>) :
    RecyclerView.Adapter<YourPatientAdapter.YourPatientViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUid = auth.currentUser?.uid


    class YourPatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientGender: TextView = itemView.findViewById(R.id.patientGender)
        val patientPhone: TextView = itemView.findViewById(R.id.patientPhone)
        val doneButton: Button = itemView.findViewById(R.id.doneButton)
        val slotTiming: TextView=itemView.findViewById(R.id.slotTiming)

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
        holder.slotTiming.text = "${patient.hours}"
        holder.doneButton.setOnClickListener {
            showAcceptConfirmationDialog(holder,patient)
        }

    }

    override fun getItemCount(): Int {
        return patients.size
    }

    private fun showAcceptConfirmationDialog(holder: YourPatientViewHolder, patient: Patient) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Done Appointment")
            .setMessage("Are you sure ${patient.name}'s appointment is Done?")
            .setPositiveButton("Yes") { _, _ ->
                storePatientData(patient)
                deletePatient(patient.patientId, holder.adapterPosition)
                notifyDataSetChanged()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deletePatient(patientId: String, position: Int) {
        val patientsCollection = db.collection("patients")
        patientsCollection.whereEqualTo("patientId", patientId).get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {

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
    private fun storePatientData(patient: Patient) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            val patientsCollection = db.collection("confirm_patients")
            val id = System.currentTimeMillis().toString()
            val newPatient = hashMapOf(
                "id" to id,
                "doctorUid" to currentUid,
                "doctorName" to patient.doctorName,
                "patientUid" to patient.uid,
                "name" to patient.name,
                "hours" to patient.hours,
                "phone" to patient.phone,
                "hospital" to patient.hospital
            )


            patientsCollection.add(newPatient)
                .addOnSuccessListener { documentReference ->

                    Log.d(TAG, "Patient data added successfully: ${documentReference.id}")


                }
                .addOnFailureListener { e ->
                    // Error adding patient data
                    Log.w(TAG, "Error adding patient data", e)
                }
        }
    }
}
