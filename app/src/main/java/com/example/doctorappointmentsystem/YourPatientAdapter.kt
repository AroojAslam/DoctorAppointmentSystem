import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.Patient
import com.example.doctorappointmentsystem.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class  YourPatientAdapter(private var patients: List<Patient>) :
    RecyclerView.Adapter<YourPatientAdapter.YourPatientViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid

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

                    showAcceptNotification(holder.itemView.context, patient.name,patient.uid)

                notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAcceptNotification(context: Context, patientName: String, patientId: String) {

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification("Appointment Accepted", "$patientName's appointment has been accepted.", patientId )
    }

    private fun showRejectNotification(context: Context, patientName: String, patientId: String) {
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification("Appointment Rejected", "$patientName's appointment has been rejected.", patientId )
    }
    class NotificationHelper(private val context: Context) {

        companion object {
            private const val CHANNEL_ID = "appointment_channel"
            private const val CHANNEL_NAME = "Appointment Notifications"
            private const val CHANNEL_DESCRIPTION = "Notifications for appointment status"
        }

        private var notificationIdCounter = 0

        init {
            createNotificationChannel()
        }

        private fun createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = CHANNEL_DESCRIPTION
                }

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                notificationManager.createNotificationChannel(channel)
            }
        }

        fun showNotification(title: String, message: String, patientId: String) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_doctor)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val notificationId = patientId.hashCode()
            notificationManager.notify(notificationId, builder.build())
        }
    }

    private fun showRejectConfirmationDialog(holder: YourPatientViewHolder, patient: Patient) {
        AlertDialog.Builder(holder.itemView.context)
            .setTitle("Reject Patient")
            .setMessage("Are you sure you want to reject ${patient.name}'s appointment?")
            .setPositiveButton("Reject") { _, _ ->
                showRejectNotification(holder.itemView.context, patient.name,patient.patientId)
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
