package com.kehnestudio.procrastinator_proccy.data.offline.online

import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistory

data class UserFirestore(
    var userId: String? = null,
    val name: String? = null,
    val score_history: List<ScoreHistoryFirestore>? = null
)
