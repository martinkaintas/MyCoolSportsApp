package com.tripple_d.mycoolsportsapp.ui.data.matches

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.MultiSelectSpinner
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.models.Match.Participation
import com.tripple_d.mycoolsportsapp.models.Sport
import java.sql.Timestamp
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MatchEditFragment : Fragment() {
    private lateinit var mainActivity: NavDrawer
    private lateinit var competitors: MutableList<Competitor>
    private lateinit var chosenCompetitors: MutableList<Competitor>
    private var match: Match? = null
    private lateinit var participantAdapter: MatchScoreListAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as NavDrawer
        val matchView = inflater.inflate(R.layout.fragment_admin_match_edit, container, false)
        match = arguments?.getParcelable<Match>("match") as Match

        setFormOptions(matchView)
            matchView?.findViewById<Button>(R.id.btCancelSport)
                ?.setOnClickListener { cancelEdit(matchView) }
            matchView?.findViewById<Button>(R.id.btEditSport)
                ?.setOnClickListener { onSubmit(matchView) }

        return matchView
    }

    private fun setFormOptions(matchView: View) {
        val sportSpinner = matchView?.findViewById<Spinner>(R.id.spAdminSport)
        val sportArray: List<Sport> = mainActivity.room_db.sportDao().getAll()
        val sportTitleArray: ArrayList<String> = sportArray.map { sport -> sport.name } as ArrayList<String>

        //disable if no competitors are selected
        matchView?.findViewById<Spinner>(R.id.spAdminPlace).isEnabled = false

        this.context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item,sportTitleArray) }.also {
                adapter ->
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sportSpinner.adapter = adapter
        }

        sportSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val competitorSpinner = matchView.findViewById(R.id.mspCompetitors) as MultiSelectSpinner
                val chosenSport = sportArray[position]
                competitors =
                    if(chosenSport.type=="team") mainActivity.room_db.teamDao().getBySport(chosenSport.id).toMutableList()
                    else mainActivity.room_db.athleteDao().getBySport(chosenSport.id).toMutableList()

                val competitorStatus = matchView.findViewById(R.id.tvAdminCompetitorStatus) as TextView
                competitorStatus.text = "0 / ${chosenSport.total_competitors}"
                competitorSpinner.setMaxItems(chosenSport.total_competitors)

                competitorSpinner.setItemSelectedCallback { choices ->
                    updateScoreBoard(matchView,chosenSport,choices as List<String>)

                    // Show total selections
                    val totalChoices = (choices as List<Int>).size
                    competitorStatus.text = "${totalChoices} / ${chosenSport.total_competitors}"

                    // Not enough choices handle
                    if(totalChoices<chosenSport.total_competitors)
                        if(totalChoices==0)
                        else competitorStatus.setTextColor(Color.RED)

                    setAvailablePlaces(choices, matchView)
                }
                competitorSpinner.setItems(competitors.map { competitor->competitor.name })
            }

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onSubmit(matchView: View) {
            createMatch(matchView)

//            updateMatch(sportView, selectedSportId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMatch(matchView: View) {
        //Sport
        val sportSpinner = matchView?.findViewById<Spinner>(R.id.spAdminSport)
        val sport = mainActivity.room_db.sportDao().getByName(sportSpinner.selectedItem.toString())

        //City
        val citySpinner = matchView?.findViewById<Spinner>(R.id.spAdminPlace)
        val city = mainActivity.room_db.cityDao().findByName(citySpinner.selectedItem.toString())

        //Date
        val dPicker = matchView?.findViewById<DatePicker>(R.id.dpMatchDate)
        val date = Timestamp.valueOf("${dPicker.year}-${dPicker.month}-${dPicker.dayOfMonth} 00:00:00")

        //participations
        val participations = arrayListOf(hashMapOf<String, Any?>())
        for(participation in participantAdapter.dataParticipants){
            val participationHash = hashMapOf<String, Any?>()
            participationHash["id"] = participation.competitor.id
            participationHash["score"] = participation.score
            if(participations[0].isEmpty())
                participations[0] = (participationHash)
            else participations.add(participationHash)
        }



        val add = HashMap<String,Any>()
        add["city"] = city.id
        add["date"] = date
        add["sport_id"] = sport.id
        add["participants"] = participations
        add["id"] = city.id+sport.id+participantAdapter.dataParticipants[0].competitor.id
        mainActivity.firebase_db.collection("Matches").add(add)
    }

    private fun setAvailablePlaces(choices: List<Int>,matchView: View){
        val placesIds = mutableListOf<Long>()
        for (choice in choices){
            competitors[choice].city_id?.let { placesIds.add(it) }
        }

        val placeSpinner = matchView?.findViewById<Spinner>(R.id.spAdminPlace)

        val cityArray: List<City> = mainActivity.room_db.cityDao().loadAllByIds(placesIds)
        val cityTitleArray: ArrayList<String> = cityArray.map { sport -> sport.name } as ArrayList<String>

        matchView?.findViewById<Spinner>(R.id.spAdminPlace).isEnabled = true

        this.context?.let { ArrayAdapter<String>(it, android.R.layout.simple_spinner_item,cityTitleArray) }.also {
                adapter ->
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            placeSpinner.adapter = adapter
        }
    }


    private fun setChosenCompetitors(matchView: View,sport: Sport,choices:List<String>){
        val competitorSpinner = matchView?.findViewById<MultiSelectSpinner>(R.id.mspCompetitors)
        if(sport.type=="team")
            chosenCompetitors =
                mainActivity.room_db.teamDao().getByNames(competitorSpinner.selectedStrings).toMutableList()
        else{
            val firstNames = mutableListOf<String>()
            val lastNames = mutableListOf<String>()
            for(name in competitorSpinner.selectedStrings){ name
                val nameArray: List<String> = name.split(" ")
                firstNames.add(nameArray[0])
                lastNames.add(nameArray[1])
            }
            chosenCompetitors = mainActivity.room_db.athleteDao().getByNames(firstNames,lastNames).toMutableList()
        }
    }

    private fun  updateScoreBoard(matchView: View,chosenSport:Sport,choices:List<String>){
        val recyclerView = matchView.findViewById<RecyclerView>(R.id.rvAdminScore)
        val participations = mutableListOf<Participation>()
        setChosenCompetitors(matchView,chosenSport, choices)

        for (competitor in chosenCompetitors){
            participations.add(Participation(0,competitor))
        }

        participantAdapter = MatchScoreListAdapter(participations)
        recyclerView.apply {
            adapter = participantAdapter
        }
        participantAdapter.notifyDataSetChanged()
    }

    private fun cancelEdit(matchView: View) {
        matchView.findNavController()
            .navigate(R.id.action_sportEditFragment_to_dataFragment, null)
    }
}