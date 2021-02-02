package com.kehnestudio.procrastinator_proccy.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_SHOW_GOALS_FRAGMENT
import com.kehnestudio.procrastinator_proccy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_mainactivity.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainactivity)

        navigateToTrackingFragmentIfNeeded(intent)

        bottomNavigationView.setupWithNavController(
            Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )
        )
        bottomNavigationView.setOnNavigationItemReselectedListener { }
        nav_host_fragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_home, R.id.fragment_goals, R.id.fragment_progress -> {
                    bottomNavigationView.visibility = View.VISIBLE
                    topAppBar.visibility = View.VISIBLE
                }

                else -> {
                    bottomNavigationView.visibility = View.GONE
                    topAppBar.visibility = View.GONE

                }
            }
        }

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_bar_item_more -> {
                    nav_host_fragment.findNavController().navigate(R.id.fragment_backdrop)
                    true
                }
                else -> false
            }
        }

    }

    override fun onBackPressed() {

        val idHost = nav_host_fragment.findNavController().currentDestination?.getId()

        when (idHost) {
            R.id.fragment_home -> finish()
            R.id.fragment_about -> nav_host_fragment.findNavController()
                .navigate(R.id.fragment_backdrop)
            R.id.fragment_faq -> nav_host_fragment.findNavController()
                .navigate(R.id.fragment_backdrop)
            R.id.fragment_myaccount -> nav_host_fragment.findNavController()
                .navigate(R.id.fragment_backdrop)
            R.id.fragment_settings -> nav_host_fragment.findNavController()
                .navigate(R.id.fragment_backdrop)
            else -> nav_host_fragment.findNavController().navigate(R.id.fragment_home)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        var mAuth = FirebaseAuth.getInstance()
        if (intent?.action == ACTION_SHOW_GOALS_FRAGMENT) {
            nav_host_fragment.findNavController().navigate(R.id.action_global_goalsFragment)
        } else if (mAuth.currentUser == null) {
            nav_host_fragment.findNavController().navigate(R.id.fragment_home)
        } else if (mAuth.currentUser != null) {
            nav_host_fragment.findNavController().navigate(R.id.fragment_login)
        }
    }
}