package com.tripple_d.mycoolsportsapp.ui.home

import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match


class MatchAdapter(private val dataSet: MutableList<Match>, private val itemClickListener: IItemClickListener):
                RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(match: Match, clickListener: IItemClickListener)
        {
            itemView.setOnClickListener {
                clickListener.onItemClicked(match)
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MatchViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.match_card_item, viewGroup, false)

        return MatchViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.participant).text = dataSet[position].participants.get(0).name
        holder.itemView.findViewById<TextView>(R.id.participant2).text = dataSet[position].participants.get(1).name
        holder.itemView.findViewById<TextView>(R.id.score).text = dataSet[position].participants.get(0).score.toString()
        holder.itemView.findViewById<TextView>(R.id.score2).text = dataSet[position].participants.get(1).score.toString()

        var maxScore: Int = 0
        var winnerIdx: Int = 0
        var idx: Int = 0
        for (participant in dataSet[position].participants) {
            if (participant.score > maxScore) {
                maxScore = participant.score
                winnerIdx = idx
            }
            idx++
        }
        holder.itemView.findViewById<TextView>(R.id.participant).setTypeface(null, Typeface.BOLD); //should make the winner Bold
        holder.itemView.findViewById<TextView>(R.id.score).setTypeface(null, Typeface.BOLD); //should make the winner Bold

        holder.itemView.findViewById<TextView>(R.id.match_date).text = dataSet[position].date.dayOfMonth.toString() + "/" + dataSet[position].date.monthValue.toString()
        holder.itemView.findViewById<TextView>(R.id.match_time).text = dataSet[position].date.hour.toString() + ":" + dataSet[position].date.minute.toString()

        holder.bind(dataSet[position],itemClickListener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}