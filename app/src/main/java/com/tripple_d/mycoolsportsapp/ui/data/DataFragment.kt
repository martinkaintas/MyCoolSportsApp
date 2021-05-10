package com.tripple_d.mycoolsportsapp.ui.data

import TeamsFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.data.athletes.AthletesFragment
import com.tripple_d.mycoolsportsapp.ui.data.matches.MatchesFragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tripple_d.mycoolsportsapp.ui.data.sports.SportsFragment

class DataFragment : Fragment() {

    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_data, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(SportsFragment(), "ΑΘΛΗΜΑΤΑ")
        adapter.addFragment(AthletesFragment(), "ΑΘΛΗΤΕΣ")
        adapter.addFragment(TeamsFragment(), "ΟΜΑΔΕΣ")
        adapter.addFragment(MatchesFragment(), "ΑΓΩΝΕΣ")
        viewPager = view.findViewById<ViewPager>(R.id.vpData)
        viewPager.adapter = adapter

        val tabLayout = view.findViewById<TabLayout>(R.id.tlData)
        tabLayout.setupWithViewPager(viewPager)
    }

}