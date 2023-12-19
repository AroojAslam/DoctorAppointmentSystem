package com.example.doctorappointmentsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

//        if (currentUser != null) {
//
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main_fragment, HomePage())
//                .commit()
//        } else {
//
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main_fragment, StartPage())
//                .commit()
//        }
    }

}