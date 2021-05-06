package com.tripple_d.mycoolsportsapp.models

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tripple_d.mycoolsportsapp.models.AthleteDAO.AthleteDao
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Competitor.City.CityDao
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.models.SportDAO.SportDao
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.TeamDao
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete

@Database(entities = [Sport::class,Athlete::class, Team::class, City::class], version = 1)
abstract class CoolDatabase : RoomDatabase() {
    public abstract fun sportDao(): SportDao
    public abstract fun teamDao(): TeamDao
    public abstract fun athleteDao(): AthleteDao
    public abstract fun cityDao(): CityDao
}