package com.tripple_d.mycoolsportsapp.ui.home

import com.tripple_d.mycoolsportsapp.models.Match

interface IItemClickListener {
        fun onItemClicked(match: Match)
}