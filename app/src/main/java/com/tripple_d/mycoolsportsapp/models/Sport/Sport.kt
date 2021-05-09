package com.tripple_d.mycoolsportsapp.models
import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class Sport(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "sex") val sex: String,
):Parcelable
