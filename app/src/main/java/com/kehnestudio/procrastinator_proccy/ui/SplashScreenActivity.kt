package com.kehnestudio.procrastinator_proccy.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.kehnestudio.procrastinator_proccy.Constants
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_SHOW_HOME_FRAGMENT
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_SHOW_LOGIN_FRAGMENT
import dagger.hilt.android.qualifiers.ApplicationContext

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        openNextActivity()
    }

    private fun openNextActivity() {
        val user = mAuth.currentUser
        val intent = Intent(this, MainActivity::class.java)

        if (user != null) {
            intent.also {
                it.action = ACTION_SHOW_HOME_FRAGMENT
            }
            startActivity(intent)
        } else {
            intent.also {
                it.action = ACTION_SHOW_LOGIN_FRAGMENT
            }
            startActivity(intent)
        }
    }
}