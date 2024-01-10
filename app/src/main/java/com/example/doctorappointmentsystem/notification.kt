package com.example.doctorappointmentsystem

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class notification : Fragment() {

    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var notificationAdapter: YourNotificationAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      val view=inflater.inflate(R.layout.fragment_notification, container, false)
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)?.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)?.supportActionBar?.title = ""
        val backicon: ImageButton = view.findViewById(R.id.backicon)
        backicon.setOnClickListener {
            findNavController().navigate(R.id.action_notification_to_homePage)
        }

        notificationRecyclerView = view.findViewById(R.id.notificationRecyclerView)

                notificationAdapter = YourNotificationAdapter(emptyList())
                notificationRecyclerView.adapter = notificationAdapter
                notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())

                loadNotifications()

                return view
            }

            private fun loadNotifications() {

                val dummyNotifications = listOf(
                    NotificationItem("Appointment Accepted", "Dr. XYZ has accepted your appointment.", System.currentTimeMillis()),
                    NotificationItem("Appointment Rejected", "Dr. ABC has rejected your appointment.", System.currentTimeMillis())
                )

                // Update RecyclerView with loaded notifications
                notificationAdapter.updateNotifications(dummyNotifications)
            }

}