package com.tripple_d.mycoolsportsapp.ui.match_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Participation

class ParticipantFragment(val participants: List<Participation>):Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_match_details, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvParticipant)
        recyclerView.apply { adapter=ParticipantAdapter(participants) }
        return root
    }
}