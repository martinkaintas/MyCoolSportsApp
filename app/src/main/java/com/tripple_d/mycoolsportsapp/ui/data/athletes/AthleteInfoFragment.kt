package com.tripple_d.mycoolsportsapp.ui.data.athletes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete

class AthleteInfoFragment() : Fragment() {

    private lateinit var mainActivity: NavDrawer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as NavDrawer
        val athleteView = inflater.inflate(R.layout.fragment_athlete_info, container, false)
        val athlete: Athlete = arguments?.getParcelable<Athlete>("athlete") as Athlete
        athleteView?.findViewById<Button>(R.id.btDeleteSport)
            ?.setOnClickListener { deleteAthlete(athleteView, athlete) }
        setAthleteInfo(athleteView, athlete)
        return athleteView
    }


    private fun setAthleteInfo(athleteView: View, athlete: Athlete) {
        val athleteName = athleteView?.findViewById<TextView>(R.id.tvSportName)
        athleteName?.text = athlete.first_name

        val athleteSurname = athleteView?.findViewById<TextView>(R.id.tvAthleteSurname)
        athleteSurname?.text = athlete.last_name

        val athleteBirthYear = athleteView?.findViewById<TextView>(R.id.spSportGender)
        athleteBirthYear?.text = athlete.birth_year.toString()

        val cityModel = mainActivity.room_db.cityDao().get(athlete.city_id)
        val teamCity = athleteView?.findViewById<TextView>(R.id.tvAthleteCity)
        teamCity?.text = cityModel.name
        val teamCountry = athleteView?.findViewById<TextView>(R.id.tvAthleteCountry)
        teamCountry?.text = cityModel.country

        val sportModel = mainActivity.room_db.sportDao().get(athlete.sport_id)
        val teamSport = athleteView?.findViewById<TextView>(R.id.spSportType)
        teamSport?.text = sportModel.name

    }

    private fun deleteAthlete(athleteView: View, athlete: Athlete) {
        mainActivity.room_db.athleteDao().delete(athlete)
        athleteView.findNavController()
            .navigate(R.id.action_athleteInfoFragment_to_dataFragment, null)
    }
}