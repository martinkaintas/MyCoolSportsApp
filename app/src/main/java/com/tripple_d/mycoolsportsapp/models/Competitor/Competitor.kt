package com.tripple_d.mycoolsportsapp.models.Competitor

abstract  class Competitor(
    open val id: Long,
    open var name: String,
    open val city_id: Long?,
    open val sport_id:Long?,
)