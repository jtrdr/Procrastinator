package com.kehnestudio.procrastinator_proccy.ui.backdrop.about


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.databinding.FragmentAboutBinding
import com.kehnestudio.procrastinator_proccy.utilities.UploadWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_about) {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AboutViewModel by viewModels()

    companion object {
        const val KEY_VALUE = "key_count"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewAbout.setOnClickListener {
            viewModel.sendPeriodicWorkRequest(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //clears reference to binding, view is cleaned up in memory
        _binding = null
    }
}