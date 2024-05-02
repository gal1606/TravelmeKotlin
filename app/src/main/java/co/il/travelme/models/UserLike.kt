package co.il.travelme.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class UserLike(

    @PrimaryKey @ColumnInfo(name = "id")
    val tripid: String
)