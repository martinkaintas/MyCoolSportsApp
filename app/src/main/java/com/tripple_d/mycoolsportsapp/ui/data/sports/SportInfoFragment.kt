package com.tripple_d.mycoolsportsapp.ui.data.sports

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
import com.tripple_d.mycoolsportsapp.models.Sport

class SportInfoFragment : Fragment() {

    private lateinit var mainActivity: NavDrawer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as NavDrawer

        val sport: Sport = arguments?.getParcelable<Sport>("sport") as Sport
        val sportInfoView = inflater.inflate(R.layout.fragment_sport_info, container, false)
        sportInfoView?.findViewById<Button>(R.id.btDeleteSport)
            ?.setOnClickListener { deleteSport(sportInfoView, sport) }
        sportInfoView?.findViewById<Button>(R.id.btEditSport)
            ?.setOnClickListener { editSport(sportInfoView, sport) }
        setAthleteInfo(sportInfoView, sport)
        return sportInfoView
    }


    private fun setAthleteInfo(athleteView: View, sport: Sport) {
        val athleteName = athleteView?.findViewById<TextView>(R.id.tvSportName)
        athleteName?.text = sport.name

        val athleteSurname = athleteView?.findViewById<TextView>(R.id.spSportType)
        athleteSurname?.text = sport.type.capitalize()

        val athleteBirthYear = athleteView?.findViewById<TextView>(R.id.spSportGender)
        athleteBirthYear?.text = sport.sex.capitalize()
    }

    private fun deleteSport(sportView: View, sport: Sport) {
        mainActivity.room_db.sportDao().delete(sport)
        sportView.findNavController()
            .navigate(R.id.action_sportInfoFragment_to_dataFragment, null)
    }

    private fun editSport(sportView: View, sport: Sport) {
        val bundle = Bundle()
        bundle.putParcelable("sport", sport)
        sportView.findNavController()
            .navigate(R.id.action_sportInfoFragment_to_sportEditFragment, bundle)
    }

}