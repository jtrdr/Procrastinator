package com.kehnestudio.procrastinator_proccy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    /*
    This property is only valid between onCreateView and onDestroyView -
    as those views will only be available when visible to user
    !! will enforce the type of binding property to non-null
     */
    private val binding get()= _binding!!
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        //binding.root is property on all automatic generated viewbinding classes. Root return whole layout

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayNameAndScore()
    }


    private fun displayNameAndScore(){

        viewModel.getUserWithScoreHistory()?.observe(viewLifecycleOwner, Observer {
            Timber.d("Observing dailyScore%s", it)
        })

        viewModel.getSpecificUser()?.observe(viewLifecycleOwner, Observer {

            Timber.d("Observing username%s", it)
            binding.textViewDisplayname.text = getString(R.string.home_fragment_displayname, it.name)
            var score = it.totalScore
            if (it.totalScore==null){
                score = 0
            }
            binding.textViewTotalScoreDisplay.text = getString(R.string.textview_score_total, score)
        })
    }
}