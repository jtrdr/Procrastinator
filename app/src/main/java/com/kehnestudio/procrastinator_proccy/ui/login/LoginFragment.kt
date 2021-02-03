package com.kehnestudio.procrastinator_proccy.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kehnestudio.procrastinator_proccy.R


class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}
