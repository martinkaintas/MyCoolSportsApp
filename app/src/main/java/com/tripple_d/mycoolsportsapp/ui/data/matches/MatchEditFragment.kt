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
import kotlinx.android.synthetic.main.match_participant_item.*
import java.sql.Timestamp
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MatchEditFragment : Fragment() {
    private lateinit var mainActivity: NavDrawer
    private lateinit var competitors: MutableList<Competitor>
    private lateinit var chosenCompetitors: MutableList<Competitor>
    private var match: Match? = null
    private lateinit var participantAdapter: MatchScoreListAdapter
    private lateinit var competitorSpinner: MultiSelectSpinner
    private lateinit var dPicker: DatePicker
    private lateinit var matchView: View



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as NavDrawer
        matchView = inflater.inflate(R.layout.fragment_admin_match_edit, container, false)
        match = arguments?.getParcelable<Match>("match")

        setFormOptions()
            matchView?.findViewById<Button>(R.id.btCancelSport)
                ?.setOnClickListener { goBack() }
            matchView?.findViewById<Button>(R.id.btEditMatch)
                ?.setOnClickListener { onSubmit() }

        return matchView
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFormOptions() {
        val sportSpinner = matchView?.findViewById<Spinner>(R.id.spAdminSport)
        val sportArray: List<Sport> = mainActivity.room_db.sportDao().getAll()
        val sportTitleArray: ArrayList<String> = sportArray.map { sport -> sport.name } as ArrayList<String>
        dPicker = matchView?.findViewById<DatePicker>(R.id.dpMatchDate)
        competitorSpinner = matchView.findViewById(R.id.mspCompetitors)

        //disable if no competitors are selected
        matchView?.findViewById<Spinner>(R.id.spAdminPlace).isEnabled = false


        if(match!=null) {
            val chosenSportIndex = match!!.let { sportArray.indexOf(it.sport) }
            sportSpinner.setSelection(chosenSportIndex)

            competitors =
                if (match!!.sport.type == "team") mainActivity.room_db.teamDao()
                    .getBySport(match!!.sport.id).toMutableList()
                else mainActivity.room_db.athleteDao().getBySport(match!!.sport.id).toMutableList()

            val selectedIndices = match!!.participations.map { participation ->
                competitors.indexOf(participation.competitor)
            }
            competitorSpinner.setItemSelectedCallback {
                competitorCallback(matchView,
                    match!!.sport, competitorSpinner.selectedIndicies)
            }
            competitorSpinner.setItems(competitors.map { competitor -> competitor.name })
            competitorSpinner.setSelection(selectedIndices.toIntArray())
            setAvailablePlaces()


            updateScoreBoard(match!!.sport,match!!.participations)
            dPicker.updateDate(match!!.date.year,match!!.date.month.value,match!!.date.dayOfMonth)
        }

        this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item,sportTitleArray) }.also {
                adapter ->
            adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sportSpinner.adapter = adapter
        }




        sportSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val chosenSport = sportArray[position]
                competitors =
                    if(chosenSport.type=="team") mainActivity.room_db.teamDao().getBySport(chosenSport.id).toMutableList()
                    else mainActivity.room_db.athleteDao().getBySport(chosenSport.id).toMutableList()

                val totalChosenCompetitors = if(match!=null) match!!.participations.size else 0
                val competitorStatus = matchView.findViewById(R.id.tvAdminCompetitorStatus) as TextView
                competitorStatus.text = "$totalChosenCompetitors / ${chosenSport.total_competitors}"
                competitorSpinner.setMaxItems(chosenSport.total_competitors)
                if(match==null) {
                    competitorSpinner.setItemSelectedCallback {
                        competitorCallback(matchView,
                            chosenSport, competitorSpinner.selectedIndicies)
                    }
                    competitorSpinner.setItems(competitors.map { competitor -> competitor.name })
                }
                if(match!=null && match!!.sport != chosenSport)
                competitorSpinner.setItems(competitors.map { competitor->competitor.name })
            }

        }
    }

    private fun competitorCallback(matchView: View, chosenSport: Sport, choices: List<Int>){
        updateScoreBoard(chosenSport,null )

        // Show total selections
        val totalChoices = choices.size
        val competitorStatus = matchView.findViewById(R.id.tvAdminCompetitorStatus) as TextView
        competitorStatus.text = "${totalChoices} / ${chosenSport.total_competitors}"

        // Not enough choices handle
        if(totalChoices<chosenSport.total_competitors)
            if(totalChoices==0)
            else competitorStatus.setTextColor(Color.RED)

        setAvailablePlaces()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onSubmit() {


        //Sport
        val sportSpinner = matchView?.findViewById<Spinner>(R.id.spAdminSport)
        val sport = mainActivity.room_db.sportDao().getByName(sportSpinner.selectedItem.toString())


        if(participantAdapter.dataParticipants.size<sport.total_competitors){
            Toast.makeText(this.context,"Υπάρχει κάποιο πρόβλημα. Ελέγξτε τα στοιχεία.",Toast.LENGTH_SHORT).show()
            return
        }

        //City
        val citySpinner = matchView?.findViewById<Spinner>(R.id.spAdminPlace)
        val city = mainActivity.room_db.cityDao().findByName(citySpinner.selectedItem.toString())

        //Date
        val dPicker = matchView?.findViewById<DatePicker>(R.id.dpMatchDate)
        val date = Timestamp.valueOf("${dPicker.year}-${dPicker.month}-${dPicker.dayOfMonth} 21:00:00")

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

        if(match == null)
            createMatch(add)
        else updateMatch(add)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createMatch(add:HashMap<String,Any>) {
        mainActivity.firebase_db.collection("Matches").add(add).addOnSuccessListener {
            Toast.makeText(this.context,"Επιτυχής Προσθήκη.",Toast.LENGTH_LONG).show()
            goBack()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateMatch(add:HashMap<String,Any>) {
        mainActivity.firebase_db.collection("Matches")
            .get()
            .addOnSuccessListener { result ->
                for (matchResult in result) {
                    if (matchResult.data["id"] == match!!.id) {
                        matchResult.id
                        mainActivity.firebase_db.collection("Matches").document(matchResult.id)
                            .update(add).addOnSuccessListener {
                                Toast.makeText(this.context,"Επιτυχής Ενημέρωση.",Toast.LENGTH_LONG).show()
                                goBack()
                            }
                        break
                    }
                }
            }
    }

    private fun setAvailablePlaces(){
        val choices = competitorSpinner.selectedIndicies
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


    private fun setChosenCompetitors(sport: Sport){
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

    private fun  updateScoreBoard(chosenSport:Sport,participations: MutableList<Participation>?){
        val recyclerView = matchView.findViewById<RecyclerView>(R.id.rvAdminScore)
        setChosenCompetitors(chosenSport)


        participantAdapter = if (participations == null) {
            val newParticipations = mutableListOf<Participation>()
            for (competitor in chosenCompetitors){
                newParticipations.add(Participation(0,competitor))
            }
            MatchScoreListAdapter(newParticipations)
        } else MatchScoreListAdapter(participations)


        recyclerView.apply {
            adapter = participantAdapter
        }
        participantAdapter.notifyDataSetChanged()
    }

    private fun goBack() {
        matchView.findNavController()
            .navigate(R.id.action_matchEditFragment_to_dataFragment, null)
    }
}