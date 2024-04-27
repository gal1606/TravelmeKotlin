package co.il.travelme.data

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 *
 * Collecting from the Flows in [PlantDao] is main-safe.  Room supports Coroutines and moves the
 * query execution off of the main thread.
 */
@Singleton
class TripsRepo @Inject constructor(private val tripDao: TripDao) {

    fun getAllTrips() = tripDao.getAllTrips()

    fun getAllTripsApplied() = tripDao.getAllTripsApplied()

    fun getTrip(id: String) = tripDao.getTrip(id)


    companion object {

        // For Singleton instantiation
        @Volatile private var instance: TripsRepo? = null

        fun getInstance(tripDao: TripDao) =
            instance ?: synchronized(this) {
                instance ?: TripsRepo(tripDao).also { instance = it }
            }
    }
}
