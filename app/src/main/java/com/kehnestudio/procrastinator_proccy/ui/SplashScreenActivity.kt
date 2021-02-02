package com.kehnestudio.procrastinator_proccy.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openNextActivity()
    }

    private fun openNextActivity() {
            val changePage = Intent(this, MainActivity::class.java)
            startActivity(changePage)
    }
}