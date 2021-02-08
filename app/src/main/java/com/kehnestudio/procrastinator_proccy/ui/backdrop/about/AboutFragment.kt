package com.kehnestudio.procrastinator_proccy.ui.backdrop.about


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.data.offline.online.UploadWorker
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : Fragment(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView_about.setOnClickListener {
            sendOneTimeWorkRequest()
        }
    }

    private fun sendOneTimeWorkRequest(){
        val uploadWorkRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java).build()
        WorkManager.getInstance(requireContext())
            .enqueue(uploadWorkRequest)
    }

}