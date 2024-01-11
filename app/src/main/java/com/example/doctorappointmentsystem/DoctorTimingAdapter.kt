import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.DoctorTiming
import com.example.doctorappointmentsystem.R
import com.google.firebase.firestore.FirebaseFirestore

class DoctorTimingAdapter(
    private val timingList: List<DoctorTiming>,
    private val context: Context,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<DoctorTimingAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var selectedPosition = RecyclerView.NO_POSITION

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHospitalName: TextView = itemView.findViewById(R.id.tvHospitalName)
        val tvHours: TextView = itemView.findViewById(R.id.tvHours)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Update the selected position
                    selectedPosition = position
                    itemClickListener.onItemClick(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_timing, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val timing = timingList[position]

        holder.tvHours.text = timing.hours
        holder.tvHospitalName.text = timing.hospital
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelected))
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int {
        return timingList.size
    }


}
