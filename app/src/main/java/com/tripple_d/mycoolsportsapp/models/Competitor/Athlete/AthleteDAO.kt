package com.tripple_d.mycoolsportsapp.models.AthleteDAO
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tripple_d.mycoolsportsapp.models.Competitor.Team.Team
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete

@Dao
interface AthleteDao {
    @Query("SELECT * FROM athlete where id==:id")
    fun get(id:Long): Athlete

    @Query("SELECT * FROM athlete")
    fun getAll(): List<Athlete>

    @Query("SELECT * FROM athlete WHERE id IN (:sportIds)")
    fun loadAllByIds(sportIds: IntArray): List<Athlete>

    @Query("SELECT * FROM athlete WHERE first_name LIKE :name")
    fun findByName(name: String): Athlete

    @Insert
    fun insertAll(vararg athletes: Athlete)

    @Delete
    fun delete(athlete: Athlete)
}
