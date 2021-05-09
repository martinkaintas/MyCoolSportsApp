package com.tripple_d.mycoolsportsapp.models.Participant.Athlete
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tripple_d.mycoolsportsapp.models.Competitor.Competitor
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Athlete(
    @PrimaryKey(autoGenerate = true) override val id: Long,
    @ColumnInfo(name = "first_name") val first_name: String,
    @ColumnInfo(name = "last_name") val last_name: String,
    @ColumnInfo(name = "city_id")  override val city_id: Long,
    @ColumnInfo(name = "sport_id") override val sport_id:Long,
    @ColumnInfo(name = "birth_year") val birth_year: Int,
):Competitor(id, "$first_name $last_name",city_id,sport_id)