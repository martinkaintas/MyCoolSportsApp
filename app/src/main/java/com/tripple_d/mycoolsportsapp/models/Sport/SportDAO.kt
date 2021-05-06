package com.tripple_d.mycoolsportsapp.models.SportDAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tripple_d.mycoolsportsapp.models.Sport

@Dao
interface SportDao {
    @Query("SELECT * FROM sport")
    fun getAll(): List<Sport>

    @Query("SELECT * FROM sport WHERE id == (:id)")
    fun get(id:Long): Sport


    @Query("SELECT * FROM sport WHERE id IN (:sportIds)")
    fun loadAllByIds(sportIds: IntArray): List<Sport>

    @Query("SELECT * FROM sport WHERE name LIKE :name")
    fun findByName(name: String): Sport

    @Insert
    fun insertAll(vararg sports: Sport)

    @Delete
    fun delete(sport: Sport)
}
