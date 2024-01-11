package com.example.doctorappointmentsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ConfirmPatientAdapter :
    ListAdapter<ConfirmPatient, ConfirmPatientAdapter.ConfirmPatientViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfirmPatientViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_confirm_patient, parent, false)
        return ConfirmPatientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConfirmPatientViewHolder, position: Int) {
        val confirmPatient = getItem(position)

        holder.patientName.text = confirmPatient.name
        holder.patientHours.text = confirmPatient.hours
        holder.patientPhone.text = confirmPatient.phone
        // Add other views as needed
    }

    class ConfirmPatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val patientName: TextView = itemView.findViewById(R.id.patientName)
        val patientHours: TextView = itemView.findViewById(R.id.patientHours)
        val patientPhone: TextView = itemView.findViewById(R.id.patientPhone)
        // Add other views as needed
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
