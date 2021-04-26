package com.tripple_d.mycoolsportsapp.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match
import com.tripple_d.mycoolsportsapp.models.Participants
import com.tripple_d.mycoolsportsapp.models.Sport
import java.time.LocalDateTime

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        var matches: MutableList<Match> = mutableListOf<Match>()

        // Dummy Data
        var nba: Sport = Sport("NBA",true)
        var miamiHeat: Participants = Participants("Miami Heat", 120)
        var memphisGrizzlies: Participants = Participants("Memphis Grizzlies", 112)
        var participants: MutableList<Participants> = mutableListOf<Participants>()
        participants.add(memphisGrizzlies)
        participants.add(miamiHeat)
        var match1: Match = Match(LocalDateTime.now(),"Miami","USA", nba, participants)

        matches.add(match1)

        val recyclerView = root.findViewById<RecyclerView>(R.id.matches_recycler)
        recyclerView.apply {
            adapter = MatchAdapter(matches)
        }

        return root
    }
}