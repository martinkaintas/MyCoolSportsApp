package com.tripple_d.mycoolsportsapp.ui.match_details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Match
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MatchDetailsFragment(val match: Match): Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        val root = inflater.inflate(R.layout.fragment_match_details, container, false)
        root.findViewById<TextView>(R.id.tvMatchSport).text = match.sport.name
        root.findViewById<TextView>(R.id.tvMatchDetailsPlace).text = match.city.name + ", " + match.city.country
        root.findViewById<TextView>(R.id.tvMatchDetailsDate).text = formatDate(match.date)


        val sortedParticipants = match.participations.sortedWith(compareByDescending({ it.score })) //sort

        val recyclerView = root.findViewById<RecyclerView>(R.id.rvParticipant)
        val participantAdapter = ParticipantAdapter(sortedParticipants)
        recyclerView.apply {
            adapter = participantAdapter
        }
        return root
    }

    private fun getGreekDay(day:String):String{
        when (day) {
            "MONDAY"-> return "Δευτέρα"
            "TUESDAY"-> return "Τρίτη"
            "WEDNESDAY"-> return "Τετάρτη"
            "THURSDAY"-> return "Πέμπτη"
            "FRIDAY"-> return "Παρασκευή"
            "SATURDAY"-> return "Σάββατο"
            }
        return "Κυριακή"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(date:LocalDateTime):String{
        var day = getGreekDay(date.dayOfWeek.toString())
        var formatter = DateTimeFormatter.ofPattern("dd/MM - HH:mm")
        var calendarDate = date.format(formatter)
        return day + " " + calendarDate
    }
}

