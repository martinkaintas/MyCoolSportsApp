package com.tripple_d.mycoolsportsapp.ui.data.athletes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete

class AthleteInfoFragment(val athlete: Athlete): Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val athleteView = inflater.inflate(R.layout.fragment_athlete_info, container, false)
        setAthleteInfo(athleteView, athlete)
        return athleteView
    }


    private fun setAthleteInfo(athleteView: View, athlete: Athlete) {
        val athleteName = athleteView?.findViewById<TextView>(R.id.tvSportName)
        athleteName?.text = athlete.first_name

        val athleteSurname = athleteView?.findViewById<TextView>(R.id.tvAthleteSurname)
        athleteSurname?.text = athlete.last_name

        val athleteBirthYear = athleteView?.findViewById<TextView>(R.id.tvSportGender)
        athleteBirthYear?.text = athlete.birth_year.toString()

    }
}