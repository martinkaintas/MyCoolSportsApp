package com.tripple_d.mycoolsportsapp.ui.data.sports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Sport

class SportInfoFragment(val sport: Sport): Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val athleteView = inflater.inflate(R.layout.fragment_sport_info, container, false)
        setAthleteInfo(athleteView, sport)
        return athleteView
    }


    private fun setAthleteInfo(athleteView: View, sport: Sport) {
        val athleteName = athleteView?.findViewById<TextView>(R.id.tvSportName)
        athleteName?.text = sport.name

        val athleteSurname = athleteView?.findViewById<TextView>(R.id.tvSportType)
        athleteSurname?.text = sport.type

        val athleteBirthYear = athleteView?.findViewById<TextView>(R.id.tvSportGender)
        athleteBirthYear?.text = sport.sex

    }
}