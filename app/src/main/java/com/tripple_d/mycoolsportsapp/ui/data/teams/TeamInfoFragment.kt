package com.tripple_d.mycoolsportsapp.ui.data.teams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.R

class TeamInfoFragment(): Fragment() {

    private lateinit var mainActivity: NavDrawer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as NavDrawer
        val team: Team = arguments?.getParcelable<Team>("team") as Team
        val teamView = inflater.inflate(R.layout.fragment_team_info, container, false)
        teamView?.findViewById<Button>(R.id.btDeleteTeam)
            ?.setOnClickListener { deleteTeam(teamView, team) }
        setTeamInfo(teamView, team)
        return teamView
    }


    private fun setTeamInfo(teamView: View, team: Team) {
        val teamName = teamView?.findViewById<TextView>(R.id.tvTeamName)
        teamName?.text = team.name

        val teamStadium = teamView?.findViewById<TextView>(R.id.tvTeamStadium)
        teamStadium?.text = team.field_name

        val teamYear = teamView?.findViewById<TextView>(R.id.tvTeamBirthYear)
        teamYear?.text = team.creation_year.toString()

        val cityModel = mainActivity.room_db.cityDao().get(team.city_id)
        val teamCity = teamView?.findViewById<TextView>(R.id.tvTeamCity)
        teamCity?.text = cityModel.name
        val teamCountry = teamView?.findViewById<TextView>(R.id.tvTeamCountry)
        teamCountry?.text = cityModel.country

        val sportModel = mainActivity.room_db.sportDao().get(team.sport_id)
        val teamSport = teamView?.findViewById<TextView>(R.id.tvTeamSport)
        teamSport?.text = sportModel.name
    }

    private fun deleteTeam(teamView: View, team: Team) {
        mainActivity.room_db.teamDao().delete(team)
        teamView.findNavController()
            .navigate(R.id.action_teamInfoFragment_to_dataFragment, null)
    }
}