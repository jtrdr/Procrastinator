package com.kehnestudio.procrastinator_proccy.ui.backdrop

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kehnestudio.procrastinator_proccy.R
import kotlinx.android.synthetic.main.activity_mainactivity.*
import kotlinx.android.synthetic.main.fragment_backdrop.*
import timber.log.Timber


class BackdropFragment : Fragment(R.layout.fragment_backdrop), View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        button_open_myaccount.setOnClickListener(this)
        button_open_settings.setOnClickListener(this)
        button_open_faq.setOnClickListener(this)
        button_open_aboutproccy.setOnClickListener(this)

    }


    override fun onClick(v: View) {

        val navController = nav_host_fragment.findNavController()
        Timber.d("onClick: %s", navController.toString())

        when (v.id) {
            R.id.button_open_aboutproccy -> {
                navController.navigate(R.id.action_fragment_backdrop_to_aboutFragment)
                Timber.d("onClick: %s", R.id.action_fragment_backdrop_to_aboutFragment)
            }
            R.id.button_open_faq -> {
                navController.navigate(R.id.action_fragment_backdrop_to_faqFragment)
                Timber.d("onClick: %s", R.id.action_fragment_backdrop_to_faqFragment)
            }
            R.id.button_open_settings -> {
                navController.navigate(R.id.action_fragment_backdrop_to_settingsFragment)
                Timber.d("onClick: %s", R.id.action_fragment_backdrop_to_settingsFragment)
            }
            R.id.button_open_myaccount -> {
                navController.navigate(R.id.action_fragment_backdrop_to_myAccountFragment)
                Timber.d("onClick: %s", R.id.action_fragment_backdrop_to_myAccountFragment)
            }
        }
    }
}