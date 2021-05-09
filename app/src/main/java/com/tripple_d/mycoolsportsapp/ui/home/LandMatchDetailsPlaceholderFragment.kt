package com.tripple_d.mycoolsportsapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.match_details.ParticipantAdapter

class LandMatchDetailsPlaceholderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_land_match_details_placeholder, container, false)
    }
}