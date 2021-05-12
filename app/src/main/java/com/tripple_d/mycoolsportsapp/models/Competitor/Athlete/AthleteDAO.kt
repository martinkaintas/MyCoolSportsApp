package com.tripple_d.mycoolsportsapp.models.AthleteDAO
import androidx.room.*
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete
import com.tripple_d.mycoolsportsapp.models.Sport
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete
import com.tripple_d.mycoolsportsapp.ui.data.matches.MatchEditFragment

@Dao
interface AthleteDao {
    @Query("SELECT * FROM athlete where id==:id")
    fun get(id:Long): Athlete

    @Query("SELECT * FROM athlete")
    fun getAll(): List<Athlete>

    @Query("SELECT * FROM athlete where sport_id==:sport_id")
    fun getBySport(sport_id:Long): List<Athlete>

    @Query("SELECT * FROM athlete where first_name IN (:first_names) AND last_name IN (:last_names)")
    fun getByNames(first_names:MutableList<String>,last_names:MutableList<String>): List<Athlete>

    @Query("SELECT * FROM athlete WHERE id IN (:sportIds)")
    fun loadAllByIds(sportIds: IntArray): List<Athlete>

    @Query("SELECT * FROM athlete WHERE first_name LIKE :name")
    fun findByName(name: String): Athlete

    @Insert
    fun insertAll(vararg athletes: Athlete)

    @Update
    fun update (vararg athlete: Athlete)

    @Delete
    fun delete(athlete: Athlete)
}
