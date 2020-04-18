package com.elouyi.bbs.fragment

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.elouyi.bbs.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings,rootKey)
    }
}