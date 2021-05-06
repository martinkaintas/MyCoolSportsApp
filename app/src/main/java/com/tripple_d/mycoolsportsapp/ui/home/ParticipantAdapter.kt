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
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import com.tripple_d.mycoolsportsapp.models.Match.Participation

class ParticipantAdapter(private val dataSet: List<Participation>):
        RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>() {

    class ParticipantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ParticipantViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.match_participant_item, viewGroup, false)

        return ParticipantViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.participant).text=dataSet[position].competitor.name
        if (dataSet[position].score >= 0){
            holder.itemView.findViewById<TextView>(R.id.score).text= dataSet[position].score.toString()
        }else{
            holder.itemView.findViewById<TextView>(R.id.score).text= ""
        }

        if(position==0) {
            holder.itemView.findViewById<TextView>(R.id.participant).setTypeface(null, Typeface.BOLD); //should make the winner Bold
            holder.itemView.findViewById<TextView>(R.id.score).setTypeface(null, Typeface.BOLD); //should make the winner Bold
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
}