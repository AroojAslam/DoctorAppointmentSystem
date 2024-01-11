package com.example.doctorappointmentsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ConfirmPatientAdapter :
    ListAdapter<ConfirmPatient, ConfirmPatientAdapter.ConfirmPatientViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmPatientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_your_patient, parent, false)
        return ConfirmPatientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConfirmPatientViewHolder, position: Int) {
        val confirmPatient = getItem(position)

        holder.patientName.text = confirmPatient.name
        holder.patientGender.visibility = View.GONE
        holder.Gender.visibility = View.GONE
        holder.patientPhone.text = confirmPatient.phone
        holder.slotTiming.text = confirmPatient.hours
        holder.doneButton.visibility = View.GONE
    }

    class ConfirmPatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientGender: TextView = itemView.findViewById(R.id.patientGender)
        val Gender: TextView = itemView.findViewById(R.id.Gender)
        val patientPhone: TextView = itemView.findViewById(R.id.patientPhone)
        val doneButton: Button = itemView.findViewById(R.id.doneButton)
        val slotTiming: TextView=itemView.findViewById(R.id.slotTiming)

    }

    private class DiffCallback : DiffUtil.ItemCallback<ConfirmPatient>() {
        override fun areItemsTheSame(oldItem: ConfirmPatient, newItem: ConfirmPatient): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ConfirmPatient, newItem: ConfirmPatient): Boolean {
            return oldItem == newItem
        }
    }
}
