package co.il.travelme.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import co.il.travelme.models.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(trips: List<Trip>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trip: Trip)

    @Update
    suspend fun update(trip: Trip)

    @Delete
    suspend fun delete(trip: Trip)

    @Query("SELECT * from trips WHERE id = :id")
    fun getTrip(id: String): Flow<Trip>

    @Query("SELECT * from trips")
    suspend fun getAllTrips(): List<Trip>

    @Query("SELECT * from trips Where pending = False")
    fun getAllTripsApplied(): Flow<List<Trip>>
}