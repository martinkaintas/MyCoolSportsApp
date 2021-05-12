package com.tripple_d.mycoolsportsapp.ui.data.matches

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Participation
import java.time.format.DateTimeFormatter

class MatchScoreListAdapter(
    var dataParticipants: MutableList<Participation>,
    )  :RecyclerView.Adapter<MatchScoreListAdapter.MatchScoreViewHolder>()  {

    inner class MatchScoreViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_match_input_participation,parent,false)
        return MatchScoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchScoreViewHolder, position: Int) {
        val score = dataParticipants[position].score
        holder.itemView.findViewById<TextView>(R.id.tvRvAdminParticipation).text=dataParticipants[position].competitor.name
        holder.itemView.findViewById<EditText>(R.id.rvInputParticipationScore).setText(""+score) //genuine keramo hacks
        holder.itemView.findViewById<EditText>(R.id.rvInputParticipationScore).setOnKeyListener { v, keyCode, event ->
            val text = holder.itemView.findViewById<EditText>(R.id.rvInputParticipationScore).text.toString()
            dataParticipants[position].score = if(text.isNotEmpty())text.toLong() else 0
            false
        }

    }



    override fun getItemCount(): Int {
        return dataParticipants.size
    }
}
