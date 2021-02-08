package com.kehnestudio.procrastinator_proccy.ui.backdrop.about


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.utilities.UploadWorker
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

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest
            .Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .build()

        val workManager: WorkManager = WorkManager
            .getInstance(requireActivity())

        workManager.enqueue(oneTimeWorkRequest)

        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(viewLifecycleOwner, Observer {
                if(it.state.isFinished){
                    val data = it.outputData
                    val dateAndTime = data.getString(UploadWorker.KEY_WORKER)
                    textView_about.text = dateAndTime
                }
            })
    }
}