import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.doctorappointmentsystem.R
import com.example.doctorappointmentsystem.Slot

class SlotsAdapter(private val onDeleteClickListener: OnDeleteClickListener) :
    ListAdapter<Slot, SlotsAdapter.SlotViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slot, parent, false)
        return SlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        val currentSlot = getItem(position)
        holder.bind(currentSlot)
        holder.deleteIcon.setOnClickListener {
            onDeleteClickListener.onDeleteClick(currentSlot)
        }
    }

    inner class SlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val hospitalNameTextView: TextView = itemView.findViewById(R.id.hospitalNameTextView)

        private val slotTimingTextView: TextView = itemView.findViewById(R.id.slotTimingTextView)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteIcon)

        fun bind(slot: Slot) {

            hospitalNameTextView.text = slot.hospitalName
            slotTimingTextView.text = slot.slotTiming
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<Slot>() {
        override fun areItemsTheSame(oldItem: Slot, newItem: Slot): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Slot, newItem: Slot): Boolean {
            return oldItem == newItem
        }
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(slot: Slot)
    }
}
