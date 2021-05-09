package com.tripple_d.mycoolsportsapp.ui.data.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.R

class TeamInfoFragment(): Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val team: Team = arguments?.getParcelable<Team>("team") as Team
        val athleteView = inflater.inflate(R.layout.fragment_team_info, container, false)
        setAthleteInfo(athleteView, team)
        return athleteView
    }


    private fun setAthleteInfo(athleteView: View, team: Team) {
        val athleteName = athleteView?.findViewById<TextView>(R.id.tvTeamName)
        athleteName?.text = team.name

    }
}