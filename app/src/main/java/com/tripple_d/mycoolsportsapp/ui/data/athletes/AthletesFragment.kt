package com.tripple_d.mycoolsportsapp.ui.data.athletes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.data.AthleteListAdapter

class AthletesFragment : Fragment() {

    private lateinit var dataViewModel: AthleteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(AthleteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_athletes, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvAthletes)
        recyclerView.apply {
            adapter = AthleteListAdapter(
                mutableListOf<String>("Takis", "Makis", "Sakis", "Lakis")
            )
        }
        return root
    }
}