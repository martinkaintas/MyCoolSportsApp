package com.tripple_d.mycoolsportsapp.ui.match_details;

import android.view.LayoutInflater
import android.view.View;
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView;
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.data.DataParticipant


class ParticipantAdapter (
    var dataParticipants:List<DataParticipant>
)  :RecyclerView.Adapter<ParticipantAdapter.ParticipantViewHolder>()  {

    inner class ParticipantViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_item_matchdetails_participation,parent,false)
        return ParticipantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.itemView.findViewById<TextView>(R.id.tvParticipant).text=dataParticipants[position].name
        holder.itemView.findViewById<TextView>(R.id.tvParticipantPoints).text= dataParticipants[position].points.toString()
    }

    override fun getItemCount(): Int {
        return dataParticipants.size
    }
}
