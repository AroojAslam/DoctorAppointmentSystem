import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.Doctor
import com.example.doctorappointmentsystem.R

class DoctorAdapter(private val doctorList: List<Doctor>) :
    RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    private var onItemClickListener: ((Doctor) -> Unit)? = null

    fun setOnItemClickListener(listener: (Doctor) -> Unit) {
        onItemClickListener = listener
    }

    class DoctorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.doctorIcon)
        val name: TextView = itemView.findViewById(R.id.doctorName)
        val type: TextView = itemView.findViewById(R.id.doctorType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctorList[position]
        holder.icon.setImageResource(R.drawable.ic_doctor)
        holder.name.text = doctor.name
        holder.type.text = doctor.type
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(doctor)
        }
    }

    override fun getItemCount(): Int {
        return doctorList.size
    }


}
