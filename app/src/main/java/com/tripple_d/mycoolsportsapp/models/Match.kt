package com.tripple_d.mycoolsportsapp.models

import java.time.LocalDateTime

data class Match (
    var date: LocalDateTime,
    var city: String,
    var country: String,
    var sport: Sport,
    var participants: List<Participants>
)