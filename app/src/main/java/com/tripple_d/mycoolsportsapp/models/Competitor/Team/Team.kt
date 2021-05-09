package com.tripple_d.mycoolsportsapp.models.Competitor.Team

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Team (
    @PrimaryKey(autoGenerate = true) override val id: Long,
    @ColumnInfo(name = "city_id") override val city_id: Long,
    @ColumnInfo(name = "sport_id") override val sport_id:Long,
    @ColumnInfo(name = "name") override var name: String,
    @ColumnInfo(name = "field_name") val field_name: String,
    @ColumnInfo(name = "creation_year") val creation_year: Int,
    ): Competitor(id,name,city_id,sport_id)