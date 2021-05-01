package com.tripple_d.mycoolsportsapp.ui.home

import com.tripple_d.mycoolsportsapp.models.Match
import com.tripple_d.mycoolsportsapp.models.Participant

interface IItemClickListener {
        fun onItemClicked(match: Match)
}