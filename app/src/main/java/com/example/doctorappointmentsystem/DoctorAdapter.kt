import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.Doctor
import com.example.doctorappointmentsystem.R

class DoctorAdapter(private var originalDoctorList: List<Doctor>) :
    RecyclerView.Adapter<DoctorAdapter.ViewHolder>() {

    private var filteredDoctorList: List<Doctor> = originalDoctorList

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val tvSpecialty: TextView = itemView.findViewById(R.id.tvDoctorSpecialty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val doctor = filteredDoctorList[position]
        holder.tvName.text = doctor.name
        holder.tvSpecialty.text = doctor.specialty

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("doctorName", doctor.name)
            bundle.putString("doctorSpecialty", doctor.specialty)
            bundle.putString("doctorAbout", doctor.about)
            bundle.putString("doctorId", doctor.doctorId)


            it.findNavController().navigate(R.id.action_homePage_to_doctorProfile, bundle)
        }
    }

    override fun getItemCount(): Int {
        return filteredDoctorList.size
    }

    fun updateDoctorList(newDoctorList: List<Doctor>, category: String) {
        originalDoctorList = newDoctorList
        filterDoctors(category)
    }

    private fun filterDoctors(category: String) {
        filteredDoctorList = if (category.isEmpty() || category == "All") {
            originalDoctorList // Show all doctors if no category selected or "All" selected
        } else {
            originalDoctorList.filter { it.specialty == category } // Filter doctors based on category
        }
        notifyDataSetChanged()
    }
}

