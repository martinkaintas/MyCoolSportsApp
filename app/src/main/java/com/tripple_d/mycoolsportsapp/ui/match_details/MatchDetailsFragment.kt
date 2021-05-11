package com.tripple_d.mycoolsportsapp.ui.match_details

import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.map.MapsActivity
import java.io.IOException
import com.tripple_d.mycoolsportsapp.models.Match.Match
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MatchDetailsFragment(): Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val match: Match = arguments?.getParcelable<Match>("match") as Match

        val root = inflater.inflate(R.layout.fragment_match_details, container, false)
        root.findViewById<TextView>(R.id.tvMatchSport).text = match.sport.name
        root.findViewById<TextView>(R.id.tvMatchDetailsPlace).text = match.city.name + ", " + match.city.country
        root.findViewById<TextView>(R.id.tvMatchDetailsDate).text = formatDate(match.date)


        val sortedParticipants = match.participations.sortedWith(compareByDescending({ it.score })) //sort

        val recyclerView = root.findViewById<RecyclerView>(R.id.rvParticipant)
        val participantAdapter = ParticipantAdapter(sortedParticipants)
        recyclerView.apply {
            adapter = participantAdapter
        }

        var showMapBtn: Button = root.findViewById<Button>(R.id.btnMatchDetailsPlaceButton)
        showMapBtn.setOnClickListener {
            showMap(match.city.name + "  " + match.city.country)
        }

        return root
    }

    fun showMap(location: String){
        val intent = Intent(activity, MapsActivity::class.java)
        var latLng = getLatLongFromLocation(location)
        val args = Bundle()
        args.putParcelable("latLng", latLng)
        intent.putExtra("bundle", args)
        startActivity(intent)
    }

    private fun getLatLongFromLocation(location: String): LatLng {
        if (Geocoder.isPresent()) {
            Toast.makeText(activity, "Waiting for LatLng data...", Toast.LENGTH_SHORT).show()
            try {
                val gc = Geocoder(activity)
                val addresses: List<Address> = gc.getFromLocationName(location, 5)
                for (a in addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        Toast.makeText(activity, "Location found.", Toast.LENGTH_SHORT).show()
                        return (LatLng(a.latitude, a.longitude))
                    }
                }
            } catch (e: IOException) {
                // handle the exception
                Toast.makeText(activity, "Location was not found.", Toast.LENGTH_SHORT).show()
                return (LatLng(39.0742 ,21.8243))
            }
        }
        return (LatLng(39.00,47.00))
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
        return "$day $calendarDate"
    }
}

