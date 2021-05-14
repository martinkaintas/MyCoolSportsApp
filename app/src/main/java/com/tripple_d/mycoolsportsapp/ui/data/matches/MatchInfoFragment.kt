package com.tripple_d.mycoolsportsapp.ui.data.matches

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.ui.match_details.ParticipantAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MatchInfoFragment : Fragment() {

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
        val mainActivity = activity as NavDrawer

        val root = inflater.inflate(R.layout.fragment_admin_match_info, container, false)
        root.findViewById<TextView>(R.id.tvMatchSport).text = match.sport.name
        root.findViewById<TextView>(R.id.tvMatchDetailsPlace).text = match.city.name + ", " + match.city.country
        root.findViewById<TextView>(R.id.tvMatchDetailsDate).text = formatDate(match.date)

        root.findViewById<TextView>(R.id.btEditMatch).setOnClickListener{
            val bundle = Bundle()
            bundle.putParcelable("match", match)
            Navigation.findNavController(root).navigate(R.id.action_MatchInfoFragment_to_MatchEditFragment, bundle)
        }
        root.findViewById<TextView>(R.id.btMatchCancel).setOnClickListener{
        mainActivity.firebase_db.collection("Matches")
            .get()
            .addOnSuccessListener { result ->
                for (matchResult in result) {
                    if (matchResult.data["id"] == match!!.id) {
                        matchResult.id
                        mainActivity.firebase_db.collection("Matches").document(matchResult.id)
                            .delete().addOnSuccessListener {
                                Toast.makeText(this.context,"Επιτυχής Διαγραφή.",Toast.LENGTH_LONG).show()
                                Navigation.findNavController(root).navigate(R.id.action_MatchInfoFragment_to_dataFragment)
                            }
                        break
                    }
                }
                }
            }


        val sortedParticipants = match.participations.sortedWith(compareByDescending { it.score }) //sort

        val recyclerView = root.findViewById<RecyclerView>(R.id.rvParticipant)
        val participantAdapter = ParticipantAdapter(sortedParticipants)
        recyclerView.apply {
            adapter = participantAdapter
        }

        return root
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
    fun formatDate(date: LocalDateTime):String{
        var day = getGreekDay(date.dayOfWeek.toString())
        var formatter = DateTimeFormatter.ofPattern("dd/MM - HH:mm")
        var calendarDate = date.format(formatter)
        return "$day $calendarDate"
    }
}