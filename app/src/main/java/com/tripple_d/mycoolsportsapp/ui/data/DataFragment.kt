package com.tripple_d.mycoolsportsapp.ui.data

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R

class DataFragment : Fragment() {

    private lateinit var dataViewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(DataViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_data, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvDataManagement)
        recyclerView.apply {
            adapter = DataListAdapter(
                mutableListOf<String>("Αθλήματα", "Ομάδες", "Αθλητές", "Αγώνες")
            )
        }
        return root
    }
}