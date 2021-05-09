package com.tripple_d.mycoolsportsapp.models.City

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.type.LatLng
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class City(
    @PrimaryKey(autoGenerate = true) val id:Long,
    @ColumnInfo(name = "name") val name:String,
    @ColumnInfo(name = "country") val country:String,
    @ColumnInfo(name = "lng") val lng:Double,
    @ColumnInfo(name = "lat") val lat:Double,
):Parcelable