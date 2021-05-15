package com.tripple_d.mycoolsportsapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.annotation.RequiresApi
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.tripple_d.mycoolsportsapp.models.*
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.models.Match.Match
import com.tripple_d.mycoolsportsapp.models.Match.Participation
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*


class NavDrawer : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    var firebase_db:FirebaseFirestore = FirebaseFirestore.getInstance()
    lateinit var room_db:CoolDatabase
    val CHANNEL_ID = "000"
    var notificationId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav_drawer)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        createNotificationChannel()

        room_db = Room.databaseBuilder(applicationContext, CoolDatabase::class.java, "cool-db").allowMainThreadQueries().build()

        if(room_db.cityDao().getAll().isEmpty()){
            room_db.cityDao().insertAll(
                City(0, "Thessaloniki", "Greece",40.629269,22.947412),
                City(0, "Athens", "Greece",37.9838,23.7275),
                City(0, "Rome", "Italy",41.54, 12.30),
                City(0, "Madrid", "Spain",40.23,3.43),
                City(0, "Berlin", "Germany", 52.31,13.23),
                City(0, "Elbasan", "Albania", 41.1102, 41.1102),
                City(0, "Miami", "Florida",25.7617 ,-80.1918),
                City(0, "Los Angeles", "California", 34.0522,118.2437 ),
                City(0, "Paris", "France", 48.8566, 2.3522),
                City(0, "London", "England", 51.5074, 0.1278),
                City(0, "Amsterdam", "Netherlands", 52.22, 4.53),
            )
        }



//        val fab: FloatingActionButton = findViewById(R.id.fab)
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_data
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_drawer, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchMatches(callback: ((MutableList<Match>) -> Unit)? = null) {
        val matches = mutableListOf<Match>()

        firebase_db.collection("Matches")
            .get()
            .addOnSuccessListener { result ->
                for (match in result) {
                    val sport = room_db.sportDao().get(match.data["sport_id"] as Long)
                    val participations : MutableList<Participation> = mutableListOf<Participation>()
                    val city = room_db.cityDao().get(match.data["city"] as Long)
                    for (participation in match.get("participants") as ArrayList<HashMap<String, String>>){
                        val competitor: Competitor = if(sport.type=="team")
                                room_db.teamDao().get(participation["id"] as Long)
                        else
                            room_db.athleteDao().get(participation["id"] as Long)

                        participations.add(Participation(participation["score"] as Long, competitor))
                    }

                    val date = LocalDateTime.ofInstant(
                        Instant.ofEpochSecond((match.data["date"] as com.google.firebase.Timestamp).seconds),
                        TimeZone.getDefault().toZoneId())
                    val matchObj = Match(
                        match.getLong("id"),
                        date,
                        city,
                        sport,
                        participations
                    )
                    // if match is today
                    if (LocalDateTime.now().until(date, ChronoUnit.DAYS)
                            .compareTo(0) == 0 && date.isAfter(LocalDateTime.now())
                    ) {
                        showMatchNotification(matchObj,
                            "${getNotificationTitle(participations)}",
                            formatDate(date),
                            getNotificationBigText(date, city, sport, participations))
                    }
                    matches.add(
                        matchObj
                    )
                }


                if (callback != null) {
                    callback(matches)
                }
            }
    }

    private fun showMatchNotification(match: Match, title: String = "Title", text: String = "Text", bigText: String = "Big Text") {
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, NavDrawer::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val args = Bundle()
        args.putParcelable("match",match)
        intent.putExtra("bundle", args)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        val builder = NotificationCompat.Builder(this, this.CHANNEL_ID)
            .setSmallIcon(R.drawable.bell)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
            notificationId ++
        }

    }

    private  fun getNotificationTitle(participations: MutableList<Participation>):String{
        var title = "${participations[0].competitor.name} vs ${participations[1].competitor.name}"
        if (participations.size > 2){
            title += " και ${participations.size - 2} ακόμη..."
        }
        return title
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun getNotificationBigText(date: LocalDateTime, city: City, sport: Sport, participations: MutableList<Participation>): String{
        var retVal: String = "${formatDate(date)} | ${city.name}, ${city.country}\n" +
                "${sport.name} / ${sport.sex.capitalize()}\n" +
                "Συμμετοχές: \n"
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
}