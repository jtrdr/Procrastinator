package com.kehnestudio.procrastinator_proccy.data.online

data class UserFirestore(
    var userId: String? = null,
    val name: String? = null,
    val score_history: List<ScoreHistoryFirestore>? = null
)
