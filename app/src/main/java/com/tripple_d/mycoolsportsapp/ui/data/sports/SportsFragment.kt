import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tripple_d.mycoolsportsapp.R
import com.tripple_d.mycoolsportsapp.ui.data.sports.SportListAdapter
import com.tripple_d.mycoolsportsapp.ui.data.sports.SportViewModel

class SportsFragment : Fragment() {

    private lateinit var dataViewModel: SportViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataViewModel =
            ViewModelProvider(this).get(SportViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_sports, container, false)
        val recyclerView = root.findViewById<RecyclerView>(R.id.rvSports)
        recyclerView.apply {
            adapter = SportListAdapter(
                mutableListOf<String>("Ποδόσφαιρο", "Μπάσκετ", "Τέννις", "Στίβος")
            )
        }
        return root
    }
}