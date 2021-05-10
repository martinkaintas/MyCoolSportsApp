package com.tripple_d.mycoolsportsapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.tripple_d.mycoolsportsapp.models.*
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete

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

        this.deleteDatabase("cool-db")
        room_db = Room.databaseBuilder(applicationContext,CoolDatabase::class.java,"cool-db").allowMainThreadQueries().build()

        if(room_db.sportDao().getAll().isEmpty()){
            room_db.sportDao().insertAll(Sport(0,"NBA", "team", "male"))
        }
        if(room_db.cityDao().getAll().isEmpty()){
            room_db.cityDao().insertAll(
                City(0,"Thessaloniki", "Greece",69.0,69.0,),
                City(0,"El Basan", "Yes",99.0,99.0,),
            )
        }

        if(room_db.teamDao().getAll().isEmpty()){
            room_db.teamDao().insertAll(
                Team(0,
                    1,
                    1,"PAOKARA","KAFKATZOGLEO",1969
                ),
                Team(0,
                    2,
                    1,"Ethniki TEI","TEI",1969
                )
            )
        }
        if(room_db.athleteDao().getAll().isEmpty()){
            room_db.athleteDao().insertAll(
                Athlete(0,"Pipis","Pipou",
                   1,
                    1,1969
                ),
                Athlete(0,"Sifis","SIfou",
                    1,
                    1,1969
                ),
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
}