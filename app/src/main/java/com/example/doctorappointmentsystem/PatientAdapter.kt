import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.Patient
import com.example.doctorappointmentsystem.R
import com.google.firebase.firestore.FirebaseFirestore

class PatientAdapter(private var patients: List<Patient>) :
    RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val appointmentNumber: TextView = itemView.findViewById(R.id.AppointmentNumber)
        val appointmentDetails: TextView = itemView.findViewById(R.id.appointmentDetails)
        val deleteAppointment: ImageButton = itemView.findViewById(R.id.deleteAppointment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        val appointmentNo = position + 1
        holder.appointmentDetails.text =
            "${patient.name} has an appointment with ${patient.doctorName} at ${patient.hospital} in slot ${patient.hours}"
        holder.appointmentNumber.text = "Appointment No ${appointmentNo.toString()}"

        holder.deleteAppointment.setOnClickListener {
            deleteAppointment(patient.patientId, position)
        }

    }

    override fun getItemCount(): Int {
        return patients.size
    }

    private fun deleteAppointment(patientId: String, position: Int) {
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
