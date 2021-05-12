package com.tripple_d.mycoolsportsapp.ui.data.athletes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R

class AthletesFragment : Fragment() {

    private lateinit var dataViewModel: AthleteViewModel
    private lateinit var mainActivity: NavDrawer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(AthleteViewModel::class.java)
        val athletesView = inflater.inflate(R.layout.fragment_athletes, container, false)
        val recyclerView = athletesView.findViewById<RecyclerView>(R.id.rvAthletes)

        athletesView?.findViewById<FloatingActionButton>(R.id.fabAddAthlete)
            ?.setOnClickListener { navigateToAddAthlete(athletesView) }
        
        mainActivity = activity as NavDrawer
        val athletes = mainActivity.room_db.athleteDao().getAll().toMutableList()
        recyclerView.apply {
            adapter = AthleteListAdapter(athletes)
        }
        return athletesView
    }

    private fun navigateToAddAthlete(athletesView: View) {
        Navigation.findNavController(athletesView)
            .navigate(R.id.action_dataFragment_to_athleteEditFragment, null)
    }
}