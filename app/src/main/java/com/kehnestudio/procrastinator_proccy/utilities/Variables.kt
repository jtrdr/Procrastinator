package com.kehnestudio.procrastinator_proccy.utilities

import timber.log.Timber
import kotlin.properties.Delegates

object Variables {
    var isNetworkConnected: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        Timber.i("Network connectivity $newValue")
    }
}