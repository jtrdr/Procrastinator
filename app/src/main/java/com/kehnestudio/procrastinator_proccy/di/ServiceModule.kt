package com.kehnestudio.procrastinator_proccy.di

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.kehnestudio.procrastinator_proccy.Constants
import com.kehnestudio.procrastinator_proccy.Constants.NOTIFICATION_CHANNEL_ID
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.ui.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideMainActivityPendingIntent(
        @ApplicationContext app: Context
    ) = PendingIntent.getActivity(
        app,
        2,
        Intent(app, MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_GOALS_FRAGMENT
        },
        FLAG_UPDATE_CURRENT
    )

    @ServiceScoped
    @Provides
    fun provideBaseNotificationBuilder(
        @ApplicationContext app: Context,
        pendingIntent: PendingIntent
    ) = NotificationCompat.Builder(app, NOTIFICATION_CHANNEL_ID)
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.ic_sloth_svg)
        .setContentTitle("Timer")
        .setContentText("00:00:00")
        .setShowWhen(false)
        .setOnlyAlertOnce(true)
        .setContentIntent(pendingIntent)

}