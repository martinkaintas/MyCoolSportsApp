package com.tripple_d.mycoolsportsapp.ui.data.matches

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R

class MatchesFragment : Fragment() {

    private lateinit var dataViewModel: MatchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(MatchViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_matches, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvMatches)
        recyclerView.apply {
            adapter = MatchListAdapter(
                mutableListOf<String>("Match 1", "Match 2", "Match 3", "Match 4")
            )
        }
        return root
    }
}