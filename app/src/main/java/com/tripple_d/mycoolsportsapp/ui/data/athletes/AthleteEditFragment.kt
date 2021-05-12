package com.tripple_d.mycoolsportsapp.ui.data.athletes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete
import com.tripple_d.mycoolsportsapp.models.Sport
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.*

class AthleteEditFragment : Fragment() {
    private lateinit var mainActivity: NavDrawer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as NavDrawer


        val athleteView = inflater.inflate(R.layout.fragment_athlete_edit, container, false)
        var selectedAthleteId: Long = -1

        setFormOptions(athleteView)

        if (arguments?.getParcelable<Athlete>("athlete") != null) {
            val athlete: Athlete = arguments?.getParcelable<Athlete>("athlete") as Athlete
            selectedAthleteId = athlete.id
            setAthleteInfo(athleteView, athlete)
        }

        // Set up year picker
        athleteView?.findViewById<ImageButton>(R.id.btAthleteYear)
            ?.setOnClickListener { showYearDialog(athleteView, selectedAthleteId) }
        athleteView?.findViewById<Button>(R.id.btCancelAthlete)
            ?.setOnClickListener { cancelEdit(athleteView) }
        athleteView?.findViewById<Button>(R.id.btSaveAthlete)
            ?.setOnClickListener { onSubmit(athleteView, selectedAthleteId) }

        return athleteView
    }


    private fun setAthleteInfo(athleteView: View, athlete: Athlete) {
        val athleteNameEditText = athleteView?.findViewById<EditText>(R.id.etAthleteName)
        athleteNameEditText.setText(athlete.first_name)

        val athleteSurnameEditText = athleteView?.findViewById<EditText>(R.id.etAthleteSurname)
        athleteSurnameEditText.setText(athlete.last_name)

        val athleteCitySpinner = athleteView?.findViewById<Spinner>(R.id.spAthleteCity)
        athleteCitySpinner.setSelection(findSelectedCityIndex(athleteCitySpinner, athlete))

        val athleteSportSpinner = athleteView?.findViewById<Spinner>(R.id.spAthleteSport)
        athleteSportSpinner.setSelection(findSelectedSportIndex(athleteSportSpinner, athlete))

        val athleteYearText = athleteView?.findViewById<TextView>(R.id.tvAthleteYear)
        athleteYearText.text = athlete.birth_year.toString()
    }


    private fun findSelectedCityIndex(citySpinner: Spinner, athlete: Athlete): Int {
        for (index in 0 until citySpinner.count) {
            val currentCity = citySpinner.getItemAtPosition(index)
            if (currentCity is City && currentCity.id == athlete.city_id) {
                return index
            }
        }
        return 0
    }


    private fun findSelectedSportIndex(sportSpinner: Spinner, athlete: Athlete): Int {
        for (index in 0 until sportSpinner.count) {
            val currentSport = sportSpinner.getItemAtPosition(index)
            if (currentSport is Sport && currentSport.id == athlete.sport_id) {
                return index
            }
        }
        return 0
    }


    private fun showYearDialog(athleteView: View, selectedAthleteId: Long) {
        val selectedYear: Int = if (selectedAthleteId < 0)
            1990
        else
            mainActivity.room_db.athleteDao().get(selectedAthleteId).birth_year
        val builder = MonthPickerDialog.Builder(
            activity,
            { _: Int, year: Int ->
                val athleteYearText = athleteView?.findViewById<TextView>(R.id.tvAthleteYear)
                athleteYearText.text = year.toString()
            },
            Calendar.YEAR,
            Calendar.MONTH
        )
        builder.showYearOnly().setYearRange(1940, 2020).setActivatedYear(selectedYear).build()
            .show()
    }

    private fun setFormOptions(athleteView: View) {
        val citySpinner = athleteView?.findViewById<Spinner>(R.id.spAthleteCity)
        val cityList = mainActivity.room_db.cityDao().getAll()
        this.activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, cityList).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                citySpinner.adapter = adapter
            }
        }

        val sportSpinner = athleteView?.findViewById<Spinner>(R.id.spAthleteSport)
        val sportList = mainActivity.room_db.sportDao().getAll().filter { it.type == "single" }
        this.activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, sportList).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                sportSpinner.adapter = adapter
            }
        }
    }


    private fun onSubmit(athleteView: View, selectedAthleteId: Long) {
        val athleteNameEditText = athleteView?.findViewById<EditText>(R.id.etAthleteName)
        val athleteName = athleteView?.findViewById<EditText>(R.id.etAthleteName).text.toString()
        if (athleteName == "") {
            athleteNameEditText.error = "Πρέπει να δωθεί όνομα αθλητή!"
            return
        }

        val athleteSurnameEditText = athleteView?.findViewById<EditText>(R.id.etAthleteSurname)
        val athleteSurname =
            athleteView?.findViewById<EditText>(R.id.etAthleteSurname).text.toString()
        if (athleteSurname == "") {
            athleteSurnameEditText.error = "Πρέπει να δωθεί επώνυμο αθλητή!"
            return
        }

        val athleteCitySpinner = athleteView?.findViewById<Spinner>(R.id.spAthleteCity)
        val athleteCity = athleteCitySpinner.selectedItem
        val athleteSport =
            athleteView?.findViewById<Spinner>(R.id.spAthleteSport).selectedItem

        val athleteBirthYear: Int =
            athleteView?.findViewById<TextView>(R.id.tvAthleteYear).text.toString().toInt()

        // Close on screen keyboard
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(athleteView.windowToken, 0)

        if (selectedAthleteId < 0) {
            if (athleteSport is Sport && athleteCity is City) {
                val newAthlete =
                    Athlete(
                        0,
                        athleteName,
                        athleteSurname,
                        athleteCity.id,
                        athleteSport.id,
                        athleteBirthYear
                    )
                createAthlete(athleteView, newAthlete)
            }
        } else {
            if (athleteSport is Sport && athleteCity is City) {
                val newAthlete =
                    Athlete(
                        selectedAthleteId,
                        athleteName,
                        athleteSurname,
                        athleteCity.id,
                        athleteSport.id,
                        athleteBirthYear
                    )
                updateAthlete(athleteView, newAthlete)
            }
        }
    }


    private fun createAthlete(athleteView: View, newAthlete: Athlete) {
        mainActivity.room_db.athleteDao().insertAll(newAthlete)

        // Show success message
        val successMessage = "Ο αθλητής δημιουργήθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()

        // Navigate to data fragment
        athleteView.findNavController()
            .navigate(R.id.action_athleteEditFragment_to_dataFragment, null)
    }


    private fun updateAthlete(athleteView: View, newAthlete: Athlete) {
        mainActivity.room_db.athleteDao().update(newAthlete)

        // Show success message
        val successMessage = "Ο αθλητής ανανεώθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()

        // Navigate to data fragment
        athleteView.findNavController()
            .navigate(R.id.action_athleteEditFragment_to_dataFragment, null)
    }


    private fun cancelEdit(athleteView: View) {
        athleteView.findNavController()
            .navigate(R.id.action_athleteEditFragment_to_dataFragment, null)
    }
}