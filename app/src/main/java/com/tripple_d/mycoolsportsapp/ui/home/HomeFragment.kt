package com.tripple_d.mycoolsportsapp.ui.home

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Intent.getIntent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.models.Match.Participation
import com.tripple_d.mycoolsportsapp.models.Sport
import com.tripple_d.mycoolsportsapp.ui.match_details.MatchDetailsFragment
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class HomeFragment : Fragment(),IItemClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mainActivity: NavDrawer
    private lateinit var matches: MutableList<Match>

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
                    val date = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond((match.data["date"] as com.google.firebase.Timestamp).seconds.toLong()),
                        TimeZone.getDefault().toZoneId())
                    val matchObj = Match(
                        match.getLong("id"),
                        date,
                        city,
                        sport,
                        participations
                    )
                    // if match is today
                    if(LocalDateTime.now().until(date, ChronoUnit.DAYS).compareTo(0) == 0 && date.isAfter(LocalDateTime.now())){
                        showMatchNotification(matchObj,"${getNotificationTitle(participations)}", formatDate(date), getNotificationBigText(date,city,sport,participations))
                    }
                    matches.add(
                        matchObj
                    )

                }

                val matchAdapter = MatchAdapter(matches, this)
                recyclerView.apply {
                    adapter = matchAdapter
                }
                matchAdapter.notifyDataSetChanged()
            }


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
        mainActivity = activity as NavDrawer

        val bundle = activity?.intent?.getParcelableExtra<Bundle>("bundle")
        val match: Match? = bundle?.getParcelable("match")
        (activity as NavDrawer).intent.removeExtra("bundle")
        if (match != null) {
            onItemClicked(match)
        }

        // Cancel previous notifications so that there are no duplicates (still spamming)
        NotificationManagerCompat.from(requireContext()).cancelAll()

        val recyclerView = root.findViewById<RecyclerView>(R.id.matches_recycler)
        mainActivity.fetchMatches {matches -> setMatchView(recyclerView,matches) }

        return root
    }

    private fun showMatchNotification(match: Match, title: String = "Title", text: String = "Text", bigText: String = "Big Text") {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(context, NavDrawer::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val args = Bundle()
        args.putParcelable("match",match)
        intent.putExtra("bundle", args)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(mainActivity, mainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(mainActivity)) {
            // notificationId is a unique int for each notification that you must define
            notify(mainActivity.notificationId, builder.build())
            mainActivity.notificationId ++
        }

    }

    private  fun getNotificationTitle(participations: MutableList<Participation>):String{
        var title = "${participations[0].competitor.name} vs ${participations[1].competitor.name}"
        if (participations.size > 2){
            title += " and ${participations.size - 2} more..."
        }
        return title
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun getNotificationBigText(date: LocalDateTime, city: City, sport: Sport, participations: MutableList<Participation>): String{
        var retVal: String = "${formatDate(date)} | ${city.name}, ${city.country}\n" +
                "${sport.name} / ${sport.sex.capitalize()}\n" +
                "Participations: \n"
        var i = 1
        for (participation in participations){
            retVal += "${i}. ${participation.competitor.name}\n"
            i++
        }


        return retVal
    }

    private fun getGreekDay(day: String):String{
        when (day) {
            "MONDAY" -> return "Δευτέρα"
            "TUESDAY" -> return "Τρίτη"
            "WEDNESDAY" -> return "Τετάρτη"
            "THURSDAY" -> return "Πέμπτη"
            "FRIDAY" -> return "Παρασκευή"
            "SATURDAY" -> return "Σάββατο"
        }
        return "Κυριακή"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDate(date: LocalDateTime):String{
        var day = getGreekDay(date.dayOfWeek.toString())
        var formatter = DateTimeFormatter.ofPattern("dd/MM - HH:mm")
        var calendarDate = date.format(formatter)
        return day + " " + calendarDate
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