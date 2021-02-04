package com.kehnestudio.procrastinator_proccy.ui.backdrop

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.services.TimerService
import kotlinx.android.synthetic.main.activity_mainactivity.*
import kotlinx.android.synthetic.main.fragment_myaccount.*


class MyAccountFragment : Fragment(R.layout.fragment_myaccount) {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        button_logout.setOnClickListener {
            logout()

        }
    }

    private fun logout(){

        val mGoogleSignInClient: GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity())
        {
            FirebaseAuth.getInstance().signOut() //signout firebase
            nav_host_fragment.findNavController().navigate(R.id.fragment_login)
            Toast.makeText(requireActivity(), "Logged Out", Toast.LENGTH_LONG).show()
        }
    }
}