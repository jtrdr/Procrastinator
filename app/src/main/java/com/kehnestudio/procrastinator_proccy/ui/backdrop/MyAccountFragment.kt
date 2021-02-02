package com.kehnestudio.procrastinator_proccy.ui.backdrop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kehnestudio.procrastinator_proccy.R

class MyAccountFragment : Fragment(R.layout.fragment_myaccount) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        container?.removeAllViews()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}