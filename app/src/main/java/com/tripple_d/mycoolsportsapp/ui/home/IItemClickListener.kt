package com.tripple_d.mycoolsportsapp.ui.home

import com.tripple_d.mycoolsportsapp.models.Match.Match

interface IItemClickListener {
        fun onItemClicked(match: Match)
}