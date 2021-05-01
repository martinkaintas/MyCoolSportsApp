import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.data.TeamListAdapter
import com.tripple_d.mycoolsportsapp.ui.data.teams.TeamViewModel

class TeamsFragment : Fragment() {

    private lateinit var dataViewModel: TeamViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(TeamViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teams, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvTeams)
        recyclerView.apply {
            adapter = TeamListAdapter(
                mutableListOf<String>("Miami Heat", "Utah Jazz", "Barcelona", "Chelsea")
            )
        }
        return root
    }
}