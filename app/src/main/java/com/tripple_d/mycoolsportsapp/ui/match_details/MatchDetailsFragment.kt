package com.tripple_d.mycoolsportsapp.ui.match_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match
import com.tripple_d.mycoolsportsapp.ui.home.MatchAdapter

class MatchDetailsFragment(val match: Match): Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_match_details, container, false)
        root.findViewById<TextView>(R.id.tvMatchSport).text = match.sport.name
        root.findViewById<TextView>(R.id.tvMatchDetailsPlace).text = match.city + ", " + match.country
        root.findViewById<TextView>(R.id.tvMatchDetailsDate).text = match.date.toString()

        val recyclerView = root.findViewById<RecyclerView>(R.id.rvParticipant)
        val participantAdapter = ParticipantAdapter(match.participants)
        recyclerView.apply {
            adapter = participantAdapter
        }
        return root
    }
}

