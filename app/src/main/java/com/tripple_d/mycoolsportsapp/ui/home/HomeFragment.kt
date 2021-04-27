package com.tripple_d.mycoolsportsapp.ui.home

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match
import com.tripple_d.mycoolsportsapp.models.Participant
import com.tripple_d.mycoolsportsapp.models.Sport
import com.tripple_d.mycoolsportsapp.ui.match_details.MatchDetailsFragment
import java.time.LocalDateTime

class HomeFragment : Fragment(),IItemClickListener {

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
        var nba: Sport = Sport("NBA", true)
        var miamiHeat: Participant = Participant("Miami Heat", 120)
        var memphisGrizzlies: Participant = Participant("Memphis Grizzlies", 112)
        var participants: MutableList<Participant> = mutableListOf<Participant>()
        participants.add(memphisGrizzlies)
        participants.add(miamiHeat)
        var match1: Match = Match(LocalDateTime.now(), "Miami", "USA", nba, participants)

        matches.add(match1)
        matches.add(match1)

        val recyclerView = root.findViewById<RecyclerView>(R.id.matches_recycler)
        val matchAdapter = MatchAdapter(matches,this)
        recyclerView.apply {
            adapter = matchAdapter
        }

        return root
    }

    override fun onItemClicked(match: Match) {
        val fr: Fragment = MatchDetailsFragment(match)
        showMatchDetailsFragment(fr)
    }

    private fun showMatchDetailsFragment(fr:Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        println(R.id.matches_recycler)
        fragmentTransaction.replace(R.id.nav_host_fragment, fr)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commitAllowingStateLoss()
    }
}