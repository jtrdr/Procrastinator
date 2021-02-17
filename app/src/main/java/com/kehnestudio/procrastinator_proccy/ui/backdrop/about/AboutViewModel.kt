package com.kehnestudio.procrastinator_proccy.ui.backdrop.about

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.kehnestudio.procrastinator_proccy.Constants.PERIODIC_WORK_UPLOAD_DATA
import com.kehnestudio.procrastinator_proccy.Constants.WORKER_INPUT_SENDER_PERIODIC
import com.kehnestudio.procrastinator_proccy.Constants.WORKER_INPUT_SOURCE
import com.kehnestudio.procrastinator_proccy.utilities.UploadWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
) : ViewModel() {
}