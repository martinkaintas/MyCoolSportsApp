import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.NavDrawer
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.data.TeamListAdapter
import com.tripple_d.mycoolsportsapp.ui.data.sports.SportListAdapter
import com.tripple_d.mycoolsportsapp.ui.data.teams.TeamViewModel

class TeamsFragment : Fragment() {

    private lateinit var dataViewModel: TeamViewModel
    private lateinit var mainActivity: NavDrawer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(TeamViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teams, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvTeams)

        mainActivity = activity as NavDrawer
        val teams = mainActivity.room_db.teamDao().getAll().toMutableList()
        recyclerView.apply {
            adapter = TeamListAdapter(teams)
        }
        return root
    }
}