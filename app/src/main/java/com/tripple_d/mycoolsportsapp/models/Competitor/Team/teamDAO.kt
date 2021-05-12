package com.tripple_d.mycoolsportsapp.models.Competitor.Team
import androidx.room.*
import com.tripple_d.mycoolsportsapp.models.Sport

@Dao
interface TeamDao {
    @Query("SELECT * FROM team where id==:id")
    fun get(id:Long): Team

    @Query("SELECT * FROM team")
    fun getAll(): List<Team>

    @Query("SELECT * FROM team where sport_id==:sport_id")
    fun getBySport(sport_id:Long): List<Team>

    @Query("SELECT * FROM team WHERE name IN (:teamNames)")
    fun getByNames(teamNames: List<String>): List<Team>

    @Query("SELECT * FROM team WHERE id IN (:teamIds)")
    fun loadAllByIds(teamIds: IntArray): List<Team>

    @Query("SELECT * FROM team WHERE name LIKE :name")
    fun findByName(name: String): Team

    @Insert
    fun insertAll(vararg teams: Team)

    @Update
    fun update (vararg team: Team)

    @Delete
    fun delete(team: Team)
}
