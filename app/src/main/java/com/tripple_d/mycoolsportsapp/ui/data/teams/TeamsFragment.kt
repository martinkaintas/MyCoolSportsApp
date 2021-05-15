package com.tripple_d.mycoolsportsapp.ui.data.teams

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
import com.tripple_d.mycoolsportsapp.ui.data.TeamListAdapter

class TeamsFragment : Fragment() {

    private lateinit var dataViewModel: TeamViewModel
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(TeamViewModel::class.java)
        val teamView = inflater.inflate(R.layout.fragment_teams, container, false)
        val recyclerView = teamView.findViewById<RecyclerView>(R.id.rvTeams)

        teamView?.findViewById<FloatingActionButton>(R.id.fabAddTeam)
            ?.setOnClickListener { navigateToAddTeam(teamView) }

        mainActivity = activity as MainActivity
        val teams = mainActivity.room_db.teamDao().getAll().toMutableList()
        recyclerView.apply {
            adapter = TeamListAdapter(teams)
        }
        return teamView
    }

    private fun navigateToAddTeam(teamView: View) {
        Navigation.findNavController(teamView)
            .navigate(R.id.action_dataFragment_to_teamEditFragment, null)
    }
}