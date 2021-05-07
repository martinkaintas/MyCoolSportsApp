package com.tripple_d.mycoolsportsapp.models.Match

import com.tripple_d.mycoolsportsapp.models.City.City
import com.tripple_d.mycoolsportsapp.models.Sport
import java.time.LocalDateTime

class Match(
    val id: Long?,
    val date: LocalDateTime,
    val city: City,
    val sport: Sport,
    val participations: MutableList<Participation>
)