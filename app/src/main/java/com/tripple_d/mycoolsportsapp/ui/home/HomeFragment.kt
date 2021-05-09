package com.tripple_d.mycoolsportsapp.ui.home

import android.content.res.Configuration
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
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.models.Match.Participation
import com.tripple_d.mycoolsportsapp.ui.match_details.MatchDetailsFragment
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

class HomeFragment : Fragment(),IItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainActivity: NavDrawer

    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchMatches(recyclerView:RecyclerView) {
        val matches: MutableList<Match> = mutableListOf<Match>()

        mainActivity.firebase_db.collection("Matches")
            .get()
            .addOnSuccessListener { result ->
                for (match in result) {
                    val sport = mainActivity.room_db.sportDao().get(match.data["sport_id"] as Long)
                    val participations : MutableList<Participation> = mutableListOf<Participation>()
                    val city = mainActivity.room_db.cityDao().get(match.data["city"] as Long)
                    for (participation in match.get("participants") as ArrayList<HashMap<String,String>>){
                        val competitor: Competitor
                        //Todo: find a better way in order to improve performance (sorry, burnout)
                        competitor = if(sport.type=="group")
                            mainActivity.room_db.teamDao().get(participation["id"] as Long)
                        else
                            mainActivity.room_db.athleteDao().get(participation["id"] as Long)

                        participations.add(Participation(participation["score"] as Long, competitor))
                    }

                    matches.add(
                        Match(
                            match.getLong("id"),
                            LocalDateTime.ofInstant(
                                Instant.ofEpochSecond((match.data["date"] as com.google.firebase.Timestamp).seconds.toLong()),
                                TimeZone.getDefault().toZoneId()),
                            city,
                            sport,
                            participations
                        )
                    )

                }

                val matchAdapter = MatchAdapter(matches, this)
                recyclerView.apply {
                    adapter = matchAdapter
                }
                matchAdapter.notifyDataSetChanged()
            }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        mainActivity = activity as NavDrawer

        val recyclerView = root.findViewById<RecyclerView>(R.id.matches_recycler)
        fetchMatches(recyclerView)

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