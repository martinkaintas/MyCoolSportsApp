package com.tripple_d.mycoolsportsapp.ui.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.ui.data.teams.TeamInfoFragment

class TeamListAdapter(
    private val dataList: MutableList<Team>
) : RecyclerView.Adapter<TeamListAdapter.DataListViewHolder>() {

    class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListViewHolder {
        return DataListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_datacard,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DataListViewHolder, position: Int) {
        holder.itemView.setOnClickListener { v ->
            val bundle = Bundle()
            bundle.putParcelable("team", dataList[position])
            Navigation.findNavController(holder.itemView).navigate(R.id.action_dataFragment_to_teamInfoFragment, bundle)
        }
        holder.itemView.findViewById<TextView>(R.id.tvDataTitle).text = dataList[position].name
    }
}