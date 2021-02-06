package com.kehnestudio.procrastinator_proccy.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_SHOW_GOALS_FRAGMENT
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_SHOW_HOME_FRAGMENT
import com.kehnestudio.procrastinator_proccy.Constants.ACTION_SHOW_LOGIN_FRAGMENT
import com.kehnestudio.procrastinator_proccy.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_mainactivity.*
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainactivity)

        if (savedInstanceState == null) {
            navigateToCorrectFragment(intent)
        }

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

        val idHost = nav_host_fragment.findNavController().currentDestination?.id

        when (idHost) {
            R.id.fragment_home, R.id.fragment_login
            -> finish()

            R.id.fragment_about, R.id.fragment_faq, R.id.fragment_myaccount, R.id.fragment_settings
            -> nav_host_fragment.findNavController().navigate(R.id.fragment_backdrop)

            else
            -> nav_host_fragment.findNavController().navigate(R.id.fragment_home)
        }
    }


    private fun navigateToCorrectFragment(intent: Intent?) {
        when (intent?.action) {
            ACTION_SHOW_GOALS_FRAGMENT -> {
                nav_host_fragment.findNavController().navigate(R.id.action_global_goalsFragment)
                Timber.d("Opened goals")
            }
            ACTION_SHOW_HOME_FRAGMENT -> {
                nav_host_fragment.findNavController().navigate(R.id.fragment_home)
                Timber.d("Opened Home")
            }
            ACTION_SHOW_LOGIN_FRAGMENT -> {
                nav_host_fragment.findNavController().navigate(R.id.fragment_login)
                Timber.d("Opened Login")
            }
        }
    }
}