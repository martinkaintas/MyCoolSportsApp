package com.tripple_d.mycoolsportsapp.ui.data.teams

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.tripple_d.mycoolsportsapp.MainActivity
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.models.Sport
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.*

class TeamEditFragment : Fragment() {
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity


        val teamView = inflater.inflate(R.layout.fragment_team_edit, container, false)
        var selectedTeamId: Long = -1

        setFormOptions(teamView)

        if (arguments?.getParcelable<Team>("team") != null) {
            val team: Team = arguments?.getParcelable<Team>("team") as Team
            selectedTeamId = team.id
            setTeamInfo(teamView, team)
        }

        // Set up year picker
        teamView?.findViewById<ImageButton>(R.id.btTeamYear)
            ?.setOnClickListener { showYearDialog(teamView, selectedTeamId) }
        teamView?.findViewById<Button>(R.id.btCancelTeam)
            ?.setOnClickListener { cancelEdit(teamView) }
        teamView?.findViewById<Button>(R.id.btSaveTeam)
            ?.setOnClickListener { onSubmit(teamView, selectedTeamId) }

        return teamView
    }


    private fun setTeamInfo(teamView: View, team: Team) {
        val teamNameEditText = teamView?.findViewById<EditText>(R.id.etTeamName)
        teamNameEditText.setText(team.name)

        val teamStadiumEditText = teamView?.findViewById<EditText>(R.id.etTeamStadium)
        teamStadiumEditText.setText(team.field_name)

        val teamCitySpinner = teamView?.findViewById<Spinner>(R.id.spTeamCity)
        teamCitySpinner.setSelection(findSelectedCityIndex(teamCitySpinner, team))

        val teamSportSpinner = teamView?.findViewById<Spinner>(R.id.spTeamSport)
        teamSportSpinner.setSelection(findSelectedSportIndex(teamSportSpinner, team))

        val teamYearText = teamView?.findViewById<TextView>(R.id.tvTeamYear)
        teamYearText.text = team.creation_year.toString()
    }


    private fun findSelectedCityIndex(citySpinner: Spinner, team: Team): Int {
        for (index in 0 until citySpinner.count) {
            val currentCity = citySpinner.getItemAtPosition(index)
            if (currentCity is City && currentCity.id == team.city_id) {
                return index
            }
        }
        return 0
    }


    private fun findSelectedSportIndex(sportSpinner: Spinner, team: Team): Int {
        for (index in 0 until sportSpinner.count) {
            val currentSport = sportSpinner.getItemAtPosition(index)
            if (currentSport is Sport && currentSport.id == team.sport_id) {
                return index
            }
        }
        return 0
    }


    private fun showYearDialog(teamView: View, selectedTeamId: Long) {
        val selectedYear: Int = if (selectedTeamId < 0)
            1930
        else
            mainActivity.room_db.teamDao().get(selectedTeamId).creation_year
        val builder = MonthPickerDialog.Builder(
            activity,
            { _: Int, year: Int ->
                val teamYearText = teamView?.findViewById<TextView>(R.id.tvTeamYear)
                teamYearText.text = year.toString()
            },
            Calendar.YEAR,
            Calendar.MONTH
        )
        builder.showYearOnly().setYearRange(1860, 2020).setActivatedYear(selectedYear).build()
            .show()
    }

    private fun setFormOptions(teamView: View) {
        val citySpinner = teamView?.findViewById<Spinner>(R.id.spTeamCity)
        val cityList = mainActivity.room_db.cityDao().getAll()
        this.activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, cityList).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                citySpinner.adapter = adapter
            }
        }

        val sportSpinner = teamView?.findViewById<Spinner>(R.id.spTeamSport)
        val sportList = mainActivity.room_db.sportDao().getAll().filter { it.type == "team" }
        this.activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, sportList).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sportSpinner.adapter = adapter
            }
        }
    }


    private fun onSubmit(teamView: View, selectedTeamId: Long) {
        val teamNameEditText = teamView?.findViewById<EditText>(R.id.etTeamName)
        val teamName = teamView?.findViewById<EditText>(R.id.etTeamName).text.toString()
        if (teamName == "") {
            teamNameEditText.error = "Πρέπει να δωθεί όνομα ομάδας!"
            return
        }

        val teamStadiumEditText = teamView?.findViewById<EditText>(R.id.etTeamStadium)
        val teamStadium =
            teamView?.findViewById<EditText>(R.id.etTeamStadium).text.toString()
        if (teamStadium == "") {
            teamStadiumEditText.error = "Πρέπει να δωθεί έδρα ομάδας!"
            return
        }

        val teamCitySpinner = teamView?.findViewById<Spinner>(R.id.spTeamCity)
        val teamCity = teamCitySpinner.selectedItem
        val teamSport =
            teamView?.findViewById<Spinner>(R.id.spTeamSport).selectedItem
        val teamCreationYear: Int =
            teamView?.findViewById<TextView>(R.id.tvTeamYear).text.toString().toInt()

        // Close on screen keyboard
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(teamView.windowToken, 0)

        if (selectedTeamId < 0) {
            if (teamSport is Sport && teamCity is City) {
                val newTeam = Team(
                    0,
                    teamCity.id,
                    teamSport.id,
                    teamName,
                    teamStadium,
                    teamCreationYear
                )
                createTeam(teamView, newTeam)
            }
        } else {
            if (teamSport is Sport && teamCity is City) {
                val newTeam = Team(
                    selectedTeamId,
                    teamCity.id,
                    teamSport.id,
                    teamName,
                    teamStadium,
                    teamCreationYear
                )
                updateTeam(teamView, selectedTeamId, newTeam)
            }
        }

    }


    private fun createTeam(teamView: View, newTeam: Team) {
        mainActivity.room_db.teamDao().insertAll(newTeam)

        // Show success message
        val successMessage = "Η ομάδα δημιουργήθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()

        // Navigate to data fragment
        teamView.findNavController()
            .navigate(R.id.action_teamEditFragment_to_dataFragment, null)
    }


    private fun updateTeam(teamView: View, selectedTeamId: Long, newTeam: Team) {
        // Add the team to the database
        mainActivity.room_db.teamDao().update(newTeam)
        // Show success message
        val successMessage = "Η ομάδα ανανεώθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()

        // Navigate to data fragment
        teamView.findNavController()
            .navigate(R.id.action_teamEditFragment_to_dataFragment, null)
    }


    private fun cancelEdit(teamView: View) {
        teamView.findNavController()
            .navigate(R.id.action_teamEditFragment_to_dataFragment, null)
    }
}