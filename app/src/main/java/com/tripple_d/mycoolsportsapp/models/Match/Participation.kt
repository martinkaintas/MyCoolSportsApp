package com.tripple_d.mycoolsportsapp.models.Match
import android.os.Parcelable
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import kotlinx.android.parcel.Parcelize

@Parcelize
class Participation (
    var score: Long,
    var competitor: Competitor
    ): Parcelable
