package com.tripple_d.mycoolsportsapp.ui.home

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.MainActivity
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.ui.match_details.MatchDetailsFragment

class HomeFragment : Fragment(),IItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainActivity: MainActivity
    private lateinit var matches: MutableList<Match>

    @RequiresApi(Build.VERSION_CODES.O)
    fun setMatchView(recyclerView:RecyclerView,matches:MutableList<Match>){
        val matchAdapter = MatchAdapter(matches, this)
        recyclerView.apply {
            adapter = matchAdapter
        }
        matchAdapter.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mainActivity = activity as MainActivity

        val bundle = activity?.intent?.getParcelableExtra<Bundle>("bundle")
        val match: Match? = bundle?.getParcelable("match")
        (activity as MainActivity).intent.removeExtra("bundle")
        if (match != null) {
            onItemClicked(match)
        }

        // Cancel previous notifications so that there are no duplicates (still spamming)
        NotificationManagerCompat.from(requireContext()).cancelAll()

        val recyclerView = root.findViewById<RecyclerView>(R.id.matches_recycler)
        mainActivity.fetchMatches {matches -> setMatchView(recyclerView,matches) }

        return root
    }

    override fun onItemClicked(match: Match) {
        val fr: Fragment = MatchDetailsFragment()
        val args = Bundle()
        args.putParcelable("match",match)
        fr.arguments = args
        showMatchDetailsFragment(fr)
    }

    private fun showMatchDetailsFragment(fr: Fragment) {
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fragmentTransaction.replace(R.id.fg_land_match_details_placeholder, fr)
        } else {
            fragmentTransaction.replace(R.id.nav_host_fragment, fr)
        }
        fragmentTransaction.addToBackStack("nav_host_fragment")
        fragmentTransaction.commitAllowingStateLoss()
    }
}