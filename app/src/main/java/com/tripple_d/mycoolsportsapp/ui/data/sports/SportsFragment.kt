package com.tripple_d.mycoolsportsapp.ui.data.sports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tripple_d.mycoolsportsapp.MainActivity
import com.tripple_d.mycoolsportsapp.R

class SportsFragment : Fragment() {

    private lateinit var dataViewModel: SportViewModel
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(SportViewModel::class.java)
        val sportsView = inflater.inflate(R.layout.fragment_sports, container, false)
        val recyclerView = sportsView.findViewById<RecyclerView>(R.id.rvSports)

        mainActivity = activity as MainActivity
        val sports = mainActivity.room_db.sportDao().getAll().toMutableList()
        recyclerView.apply {
            adapter = SportListAdapter(sports)
        }

        sportsView?.findViewById<FloatingActionButton>(R.id.fabAddSport)
            ?.setOnClickListener { navigateToAddSport(sportsView) }

        return sportsView
    }

    private fun navigateToAddSport(sportsView: View) {
        Navigation.findNavController(sportsView)
            .navigate(R.id.action_sportListFragment_to_sportEditFragment, null)
    }
}