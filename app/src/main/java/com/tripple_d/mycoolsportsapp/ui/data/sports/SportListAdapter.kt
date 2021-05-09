package com.tripple_d.mycoolsportsapp.ui.data.sports

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Sport

class SportListAdapter(
    private val dataList: MutableList<Sport>

) : RecyclerView.Adapter<SportListAdapter.DataListViewHolder>() {

    class DataListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

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
            val activity = v!!.context as AppCompatActivity
            val sportInfoFragment = SportInfoFragment(dataList[position])
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fgData, sportInfoFragment).addToBackStack("sup").commit()
        }
        holder.itemView.findViewById<TextView>(R.id.tvDataTitle).text = dataList[position].name
    }
}