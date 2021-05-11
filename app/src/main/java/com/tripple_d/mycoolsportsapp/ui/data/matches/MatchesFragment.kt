package com.tripple_d.mycoolsportsapp.ui.data.matches

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.ui.data.sports.SportListAdapter
import java.time.format.DateTimeFormatter

class MatchesFragment : Fragment() {

    private lateinit var dataViewModel: MatchViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(MatchViewModel::class.java)
        val mainActivity = activity as NavDrawer

        val root = inflater.inflate(R.layout.fragment_matches, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvMatches)
        mainActivity.fetchMatches {matches -> setFetchedMatches(recyclerView,matches) }

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setFetchedMatches(recyclerView:RecyclerView, matches:MutableList<Match>){
        recyclerView.apply {
            adapter = MatchListAdapter(
                matches
            )
        }
    }
}