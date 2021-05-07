package com.tripple_d.mycoolsportsapp.models.Competitor.Team
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TeamDao {
    @Query("SELECT * FROM team where id==:id")
    fun get(id:Long): Team

    @Query("SELECT * FROM team")
    fun getAll(): List<Team>

    @Query("SELECT * FROM team WHERE id IN (:teamIds)")
    fun loadAllByIds(teamIds: IntArray): List<Team>

    @Query("SELECT * FROM team WHERE name LIKE :name")
    fun findByName(name: String): Team

    @Insert
    fun insertAll(vararg teams: Team)

    @Delete
    fun delete(team: Team)
}
