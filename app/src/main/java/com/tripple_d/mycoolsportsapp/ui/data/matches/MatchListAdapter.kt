package com.tripple_d.mycoolsportsapp.ui.data.matches

import android.os.Build
import android.os.Bundle
import android.service.autofill.FieldClassification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.ui.data.sports.SportListAdapter
import java.time.format.DateTimeFormatter

class MatchListAdapter(
    private val dataList: MutableList<Match>
) : RecyclerView.Adapter<MatchListAdapter.DataListViewHolder>() {

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DataListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("match", dataList[position])
            Navigation.findNavController(holder.itemView).navigate(R.id.action_matchListFragment_to_MatchAdminViewFragment, bundle)
        }
        holder.itemView.findViewById<TextView>(R.id.tvDataTitle).text = formatTitle(dataList[position])

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatTitle(match:Match):String{
        val names = match.participations.map{participation->
            if(participation.competitor.name.length>10)
                participation.competitor.name.substring(0,10)+"."
            else participation.competitor.name}
        var title = "${match.sport.name} | "
        title += if(match.sport.total_competitors==2)
            "${names[0]} vs ${names[1]}"
        else
            "${names[0]} vs ${match.sport.total_competitors-1}"
        title+= ", "+match.date.format(DateTimeFormatter.ofPattern("dd/MM"))

        return title
    }
}