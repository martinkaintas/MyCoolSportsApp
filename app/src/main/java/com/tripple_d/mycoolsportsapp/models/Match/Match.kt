package com.tripple_d.mycoolsportsapp.models.Match

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Sport
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import java.time.LocalDateTime
import java.util.*

@Parcelize
class Match(
    val id: Long?,
    val date: LocalDateTime,
    val city: City,
    val sport: Sport,
    val participations: MutableList<Participation>
) : Parcelable