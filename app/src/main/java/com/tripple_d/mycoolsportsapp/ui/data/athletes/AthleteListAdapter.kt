package com.tripple_d.mycoolsportsapp.ui.data.athletes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete

class AthleteListAdapter(
    private val dataList: MutableList<Athlete>
) : RecyclerView.Adapter<AthleteListAdapter.DataListViewHolder>() {

    class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataListViewHolder {
        return DataListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_datacard, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DataListViewHolder, position: Int) {
        holder.itemView.setOnClickListener { v ->
            val activity = v!!.context as AppCompatActivity
            val athleteInfoFragment = AthleteInfoFragment(dataList[position])
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fgData, athleteInfoFragment).addToBackStack("sup").commit()
        }
        holder.itemView.findViewById<TextView>(R.id.tvDataTitle).text = dataList[position].name
    }
}