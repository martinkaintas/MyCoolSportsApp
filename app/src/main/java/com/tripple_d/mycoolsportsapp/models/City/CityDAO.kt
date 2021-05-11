package com.tripple_d.mycoolsportsapp.models.Competitor.City

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tripple_d.mycoolsportsapp.models.City.City

@Dao
interface CityDao {
    @Query("SELECT * FROM city where id LIKE :id LIMIT 1")
    fun get(id:Long): City

    @Query("SELECT * FROM city")
    fun getAll(): List<City>

    @Query("SELECT * FROM city WHERE id IN (:cityIds)")
    fun loadAllByIds(cityIds: MutableList<Long>): List<City>

    @Query("SELECT * FROM city WHERE name LIKE :name")
    fun findByName(name: String): City

    @Insert
    fun insertAll(vararg cities: City)

    @Delete
    fun delete(city: City)
}
