package com.kehnestudio.procrastinator_proccy

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.firebase.FirebaseApp
import com.kehnestudio.procrastinator_proccy.utilities.NetworkMonitor
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ProccyApplication : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        NetworkMonitor(this).startNetworkCallback()
        Timber.plant(Timber.DebugTree())
    }

    override fun onTerminate(){
        super.onTerminate()
        //Stop network callback
        NetworkMonitor(this).stopNetworkCallback()
    }

}