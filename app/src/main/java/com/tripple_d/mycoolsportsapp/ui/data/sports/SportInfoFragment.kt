package com.tripple_d.mycoolsportsapp.ui.data.sports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        setSportInfo(sportInfoView, sport)
        return sportInfoView
    }


    private fun setSportInfo(sportView: View, sport: Sport) {
        val sportName = sportView?.findViewById<TextView>(R.id.tvSportName)
        sportName?.text = sport.name

        val sportType = sportView?.findViewById<TextView>(R.id.tvSportType)
        sportType?.text = sport.type.capitalize()

        val sportParticipantNumber = sportView?.findViewById<TextView>(R.id.tvSportParticipants)
        sportParticipantNumber?.text = sport.total_competitors.toString()

        if (sport.type == "team") {
            sportParticipantNumber.visibility = View.GONE
            sportView?.findViewById<TextView>(R.id.tvSportParticipantsLabel).visibility = View.GONE
        }

        val sportGender = sportView?.findViewById<TextView>(R.id.tvSportGender)
        sportGender?.text = sport.sex.capitalize()
    }


    private fun deleteSport(sportView: View, sport: Sport) {
        mainActivity.room_db.sportDao().delete(sport)

        mainActivity.room_db.teamDao().deleteBySport(sport.id)
        mainActivity.room_db.athleteDao().deleteBySport(sport.id)

        val successMessage = "Το άθλημα διαγράφηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()
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