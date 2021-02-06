package com.kehnestudio.procrastinator_proccy.ui.backdrop.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.kehnestudio.procrastinator_proccy.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}