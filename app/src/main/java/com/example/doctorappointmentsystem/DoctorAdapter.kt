import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.Doctor
import com.example.doctorappointmentsystem.R

class DoctorAdapter(private val doctorList: List<Doctor>) :
    RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val specialtiesTextView: TextView = itemView.findViewById(R.id.specialtiesTextView)
//        val aboutTextView: TextView = itemView.findViewById(R.id.aboutTextView)
//        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDoctor = doctorList[position]

        holder.nameTextView.text = "${currentDoctor.name}"
        holder.specialtiesTextView.text = "${currentDoctor.specialties}"
//        holder.aboutTextView.text = "About: ${currentDoctor.about}"
//        holder.idTextView.text = "ID: ${currentDoctor.id}"
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }
}
