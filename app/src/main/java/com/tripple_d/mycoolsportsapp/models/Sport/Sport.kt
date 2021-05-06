package com.tripple_d.mycoolsportsapp.models
import androidx.room.*


@Entity
data class Sport(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "sex") val sex: String,
)
