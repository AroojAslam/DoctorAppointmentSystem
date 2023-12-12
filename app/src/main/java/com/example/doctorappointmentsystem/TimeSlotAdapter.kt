import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.doctorappointmentsystem.R

class TimeSlotAdapter(private val context: Context, private val timeSlots: List<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return timeSlots.size
    }

    override fun getItem(position: Int): Any {
        return timeSlots[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = inflater.inflate(R.layout.item_time_slot, parent, false)

        val timeSlotText: TextView = itemView.findViewById(R.id.timeSlotText)
        timeSlotText.text = timeSlots[position]

        return itemView
    }
}
