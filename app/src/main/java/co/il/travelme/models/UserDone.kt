package co.il.travelme.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user-dones")
data class UserDone(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val tripid: String
)