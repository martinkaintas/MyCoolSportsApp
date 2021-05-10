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
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Sport

class SportEditFragment : Fragment() {
    private lateinit var mainActivity: NavDrawer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity = activity as NavDrawer

        val sportView = inflater.inflate(R.layout.fragment_sport_edit, container, false)
        var selectedSportId : Long = -1
        setFormOptions(sportView)

        if (arguments?.getParcelable<Sport>("sport") != null) {
            val sport: Sport = arguments?.getParcelable<Sport>("sport") as Sport
            selectedSportId = sport.id
            setSportInfo(sportView, sport)
        }
        else {
        }

        sportView?.findViewById<Button>(R.id.btCancelSport)
            ?.setOnClickListener { cancelEdit(sportView) }
        sportView?.findViewById<Button>(R.id.btEditSport)
            ?.setOnClickListener { onSubmit(sportView, selectedSportId) }

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


    private fun onSubmit(sportView: View, selectedSportId: Long) {
        if (selectedSportId < 0) {
            createSport(sportView)
        }
        else{
            updateSport(sportView, selectedSportId)
        }

        // Navigate to data fragment
        sportView.findNavController()
            .navigate(R.id.action_sportEditFragment_to_dataFragment, null)
    }

    private fun createSport(sportView: View) {
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

        // Add the sport to the database
        mainActivity.room_db.sportDao().insertAll(
            Sport(0, sportName, sportType, sportGender)
        )

        // Show success message
        val successMessage = "Το άθλημα δημιουργήθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()
    }


    private fun updateSport(sportView: View, selectedSportId: Long) {
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

        // Add the sport to the database
        mainActivity.room_db.sportDao().update(
            Sport(selectedSportId, sportName, sportType, sportGender)
        )
        // Show success message
        val successMessage = "Το άθλημα ανανεώθηκε επιτυχώς!"
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(activity, successMessage, duration)
        toast.show()

    }



    private fun cancelEdit(sportView: View) {
        sportView.findNavController()
            .navigate(R.id.action_sportEditFragment_to_dataFragment, null)
    }
}