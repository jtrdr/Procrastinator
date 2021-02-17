package com.kehnestudio.procrastinator_proccy.ui.progress

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kehnestudio.procrastinator_proccy.data.offline.ScoreHistoryLocalDate
import com.kehnestudio.procrastinator_proccy.databinding.ItemScoreBinding
import timber.log.Timber
import java.time.format.DateTimeFormatter

class ScoreAdapter : ListAdapter<ScoreHistoryLocalDate, ScoreAdapter.ScoreHistoryViewHolder>(DiffCallback()) {

    val scores = mutableListOf<ScoreHistoryLocalDate>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoreHistoryViewHolder {
        val binding = ItemScoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScoreHistoryViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: ScoreHistoryViewHolder, position: Int) {

            viewHolder.bind(scores[position])
    }

    override fun getItemCount(): Int = scores.size

    class ScoreHistoryViewHolder(private val binding: ItemScoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(scoreHistory: ScoreHistoryLocalDate) {

                binding.apply {
                    textViewItemDate.text = scoreHistory.date.toString()
                    textviewItemScore.text = scoreHistory.score.toString()
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ScoreHistoryLocalDate>() {
        override fun areItemsTheSame(
            oldItem: ScoreHistoryLocalDate,
            newItem: ScoreHistoryLocalDate
        ) = oldItem.date == newItem.date

        override fun areContentsTheSame(
            oldItem: ScoreHistoryLocalDate,
            newItem: ScoreHistoryLocalDate
        ) = oldItem == newItem

    }
}