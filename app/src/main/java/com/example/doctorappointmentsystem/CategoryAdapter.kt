import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.R

class CategoryAdapter(private val dataList: List<String>, private val icons: List<String>) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override   fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horizontal_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dataList[position], icons[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.icons)
        private val textView: TextView = itemView.findViewById(R.id.textItem)

        fun bind(item: String, icon: String) {
            textView.text = item

            val resourceId = itemView.context.resources.getIdentifier(icon, "drawable", itemView.context.packageName)
            imageView.setImageResource(resourceId)
        }
    }
}
