import androidx.room.Embedded
import androidx.room.Relation
import com.tripple_d.mycoolsportsapp.models.Participant.Athlete.Athlete
import com.tripple_d.mycoolsportsapp.models.Sport

class SportWithAthletes (
    @Embedded val sport: Sport,
    @Relation(
        parentColumn = "sportId",
        entityColumn = "sportWithAthletesId"
    )
    val athletes: List<Athlete>
)