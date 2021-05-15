package com.tripple_d.mycoolsportsapp.ui.data.sports

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
import com.tripple_d.mycoolsportsapp.models.Sport
import com.whiteelephant.monthpicker.MonthPickerDialog
import java.util.*

class SportEditFragment : Fragment() {
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as MainActivity

        val sportView = inflater.inflate(R.layout.fragment_sport_edit, container, false)
        var selectedSportId: Long = -1

        setFormOptions(sportView)

        if (arguments?.getParcelable<Sport>("sport") != null) {
            val sport: Sport = arguments?.getParcelable<Sport>("sport") as Sport
            selectedSportId = sport.id
            setSportInfo(sportView, sport)
        }

        sportView?.findViewById<ImageButton>(R.id.btSportParticipants)
            ?.setOnClickListener { showParticipantPicker(sportView) }
        sportView?.findViewById<Button>(R.id.btCancelSport)
            ?.setOnClickListener { cancelEdit(sportView) }
        sportView?.findViewById<Button>(R.id.btEditAthlete)
            ?.setOnClickListener { onSubmit(sportView, selectedSportId) }

        val sportTypeSpinner = sportView?.findViewById<Spinner>(R.id.spSportType)
        sportTypeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toggleParticipantSelectorVisibility(
                    sportView,
                    parent?.getItemAtPosition(position).toString().toLowerCase()
                )
            }

        }

        return sportView
    }


    private fun setSportInfo(sportView: View, sport: Sport) {
        val sportNameEditText = sportView?.findViewById<EditText>(R.id.etSportName)
        sportNameEditText.setText(sport.name)

        val sportTypeSpinner = sportView?.findViewById<Spinner>(R.id.spSportType)
        if (sport.type == "team")
            sportTypeSpinner.setSelection(0)
        else
            sportTypeSpinner.setSelection(1)

        val sportGender = sportView?.findViewById<Spinner>(R.id.spSportGender)
        println(sport.sex)
        if (sport.sex == "male")
            sportGender.setSelection(0)
        else
            sportGender.setSelection(1)
    }


    private fun setFormOptions(sportView: View) {
        val typeSpinner = sportView?.findViewById<Spinner>(R.id.spSportType)
        this.activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.SportTypes,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typeSpinner.adapter = adapter
            }
        }

        val genderSpinner = sportView?.findViewById<Spinner>(R.id.spSportGender)
        this.activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.Genders,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                genderSpinner.adapter = adapter
            }
        }
    }


    private fun toggleParticipantSelectorVisibility(sportView: View, selectedSportType: String) {
        val participantsTextView = sportView?.findViewById<TextView>(R.id.tvSportEditParticipants)
        val participantsButton = sportView?.findViewById<ImageButton>(R.id.btSportParticipants)
        val participantsLabel = sportView?.findViewById<TextView>(R.id.tvSportEditParticipantsLabel)
        if (selectedSportType == "team") {
            participantsTextView.visibility = View.GONE
            participantsButton.visibility = View.GONE
            participantsLabel.visibility = View.GONE
        } else {
            participantsTextView.visibility = View.VISIBLE
            participantsButton.visibility = View.VISIBLE
            participantsLabel.visibility = View.VISIBLE
        }
    }


    private fun showParticipantPicker(sportView: View) {
        val participantsTextView = sportView?.findViewById<TextView>(R.id.tvSportEditParticipants)
        val builder = MonthPickerDialog.Builder(
            activity,
            { _: Int, year: Int ->
                participantsTextView.text = year.toString()
            },
            Calendar.YEAR,
            Calendar.MONTH
        )
        builder.showYearOnly().setYearRange(1, 8).setActivatedYear(2)
            .setTitle("Επιλογή αριθμού συμμετεχόντων").build()
            .show()
    }


    private fun onSubmit(sportView: View, selectedSportId: Long) {
        val sportNameEditText = sportView?.findViewById<EditText>(R.id.etSportName)
        val sportName = sportView?.findViewById<EditText>(R.id.etSportName).text.toString()
        if (sportName == "") {
            sportNameEditText.error = "Πρέπει να δωθεί όνομα αθλήματος!"
            return
        }

        val sportType =
            sportView?.findViewById<Spinner>(R.id.spSportType).selectedItem.toString().toLowerCase()
        val sportGender =
            sportView?.findViewById<Spinner>(R.id.spSportGender).selectedItem.toString()
                .toLowerCase()

        // Close on screen keyboard
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(sportView.windowToken, 0)

        val sportCompetitors =
            sportView?.findViewById<TextView>(R.id.tvSportEditParticipants).text.toString().toInt()

        val competitorNumber: Int = if (sportType == "team")
            2
        else
            sportCompetitors

        if (selectedSportId < 0) {
            val newSport = Sport(0, sportName, sportType, sportGender, competitorNumber)
            createSport(sportView, newSport)
        } else {
            val newSport =
                Sport(selectedSportId, sportName, sportType, sportGender, competitorNumber)
            updateSport(sportView, newSport)
        }
    }


    private fun createSport(sportView: View, newSport: Sport) {
        mainActivity.room_db.sportDao().insertAll(newSport)

        // Show success message
        val successMessage = "Το άθλημα δημιουργήθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()

        // Navigate to data fragment
        sportView.findNavController()
            .navigate(R.id.action_sportEditFragment_to_dataFragment, null)
    }


    private fun updateSport(sportView: View, newSport: Sport) {
        mainActivity.room_db.sportDao().update(newSport)

        // Show success message
        val successMessage = "Το άθλημα ανανεώθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()

        // Navigate to data fragment
        sportView.findNavController()
            .navigate(R.id.action_sportEditFragment_to_dataFragment, null)
    }


    private fun cancelEdit(sportView: View) {
        sportView.findNavController()
            .navigate(R.id.action_sportEditFragment_to_dataFragment, null)
    }
}