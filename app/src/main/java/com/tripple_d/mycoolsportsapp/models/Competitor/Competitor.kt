package com.tripple_d.mycoolsportsapp.models.Competitor

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class Competitor(
    open val id: Long,
    open var name: String,
    open val city_id: Long?,
    open val sport_id:Long?,
): Parcelable

