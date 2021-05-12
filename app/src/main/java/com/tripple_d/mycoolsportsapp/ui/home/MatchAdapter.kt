package com.tripple_d.mycoolsportsapp.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.models.Match.Participation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MatchAdapter(private val dataSet: MutableList<Match>, private val itemClickListener: IItemClickListener):
                RecyclerView.Adapter<MatchAdapter.MatchViewHolder>() {

    class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var participantRV: RecyclerView = itemView.findViewById(R.id.match_card_item_rv)
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
        var participantsSorted : List<Participation> = dataSet[position].participations.sortedWith(compareByDescending { it.score })
        var childRecyclerViewAdapter = ParticipantAdapter(participantsSorted)
        holder.participantRV.setAdapter(childRecyclerViewAdapter)

        var dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
        var timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        var date: LocalDateTime = dataSet[position].date
        var calendarDate = date.format(dateFormatter)
        var calendarTime = date.format(timeFormatter)
        holder.itemView.findViewById<TextView>(R.id.match_date).text = calendarDate
        holder.itemView.findViewById<TextView>(R.id.match_time).text = calendarTime

        dataSet[position].sport.name?.let { changeImage(it.toLowerCase(), holder.itemView) }

        holder.participantRV.setOnTouchListener { v, _ ->
            itemClickListener.onItemClicked(dataSet[position])
            return@setOnTouchListener true
        }

        holder.bind(dataSet[position], itemClickListener)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeImage(sport: String, view: View){
        if (sport.isNullOrBlank()) return
        val img = view.findViewById<ImageView>(R.id.matchIcon)

        val basketballIcon = view.context.resources.getDrawable(R.drawable.ic_baseline_sports_basketball_24, view.context.theme)
        val footballIcon = view.context.resources.getDrawable(R.drawable.sports_soccer, view.context.theme)
        val sportsIcon = view.context.resources.getDrawable(R.drawable.sports, view.context.theme)
        val mmaIcon = view.context.resources.getDrawable(R.drawable.sports_mma, view.context.theme)
        val runningIcon = view.context.resources.getDrawable(R.drawable.sports_running, view.context.theme)
        val tennisIcon = view.context.resources.getDrawable(R.drawable.sports_tennis, view.context.theme)
        val volleyballIcon = view.context.resources.getDrawable(R.drawable.sports_volleyball, view.context.theme)

        if (sport.contains("basket") || sport == "nba") img.setImageDrawable(basketballIcon)
        else if( sport == "football" || sport == "soccer") img.setImageDrawable(footballIcon)
        else if( sport == "mma" || sport == "box") img.setImageDrawable(mmaIcon)
        else if( sport == "running" || sport == "sprint" || sport == "marathon") img.setImageDrawable(runningIcon)
        else if( sport == "tennis") img.setImageDrawable(tennisIcon)
        else if( sport.contains("volley")) img.setImageDrawable(volleyballIcon)
        else  img.setImageDrawable(sportsIcon)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}