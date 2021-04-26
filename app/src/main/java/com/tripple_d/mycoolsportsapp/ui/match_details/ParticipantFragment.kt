package com.tripple_d.mycoolsportsapp.ui.match_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.data.DataParticipant

class ParticipantFragment:Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_match_details, container, false)

        var participantList = mutableListOf(
            DataParticipant("Makis Makou",10),
            DataParticipant("Nikos Nikas",9),
            DataParticipant("Dimitris Plikos",69)
        )
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvParticipant)
        recyclerView.apply { adapter=ParticipantAdapter(participantList) }
        return root
    }
}