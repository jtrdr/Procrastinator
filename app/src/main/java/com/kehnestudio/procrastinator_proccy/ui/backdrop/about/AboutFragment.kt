package com.kehnestudio.procrastinator_proccy.ui.backdrop.about


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.data.online.UploadWorker
import com.kehnestudio.procrastinator_proccy.data.online.UploadWorker.Companion.KEY_WORKER
import kotlinx.android.synthetic.main.fragment_about.*
import java.util.concurrent.TimeUnit


class AboutFragment : Fragment(R.layout.fragment_about) {

    companion object {
        const val KEY_VALUE = "key_count"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView_about.setOnClickListener {
            sendPeriodicWorkRequest()
        }
    }

    private fun sendPeriodicWorkRequest() {
        val periodicWorkRequest = PeriodicWorkRequest
            .Builder(UploadWorker::class.java, 15, TimeUnit.MINUTES)
            .build()

        val workManager: Operation = WorkManager
            .getInstance(requireActivity())
            .enqueueUniquePeriodicWork(
                "Data Sync",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
    }
}