package co.il.travelme.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.il.travelme.models.Trip
import com.google.firebase.firestore.FirebaseFirestore


class TripVM : ViewModel() {
    private val firebaseDBVM = FirebaseDBVM()  // ייבוא או יצירת מופע מ-FirebaseDBVM
    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips

    init {
        loadTrips()
    }

    private fun loadTrips() {
        firebaseDBVM.getTrips(
            onSuccess = { tripsList ->
                Log.i("gil","onSuccess")
                _trips.postValue(tripsList)
                Log.i("gil","onSuccess2" + tripsList.size)
            },
            onFailure = { exception ->
                Log.i("gil","onFailure")
            }
        )
    }
}
/*class TripVM @Inject internal constructor(
    tripsRepo: TripsRepo,
    *//*    userLikeRepo: UserLikeRepo,
        userDoneRepo: UserDoneRepo*//*
) : ViewModel() {*/

 //  val trips: LiveData<List<Trip>> = tripsRepo.getAllTrips().asLiveData()
/*   val tripsApplied: LiveData<List<Trip>> = tripsRepo.getAllTripsApplied().asLiveData()
   val liked: LiveData<List<UserLike>> = userLikeRepo.getAllTrips().asLiveData()
   val done: LiveData<List<UserDone>> = userDoneRepo.getAllTrips().asLiveData()*/

/*    @OptIn(DelicateCoroutinesApi::class)
    fun createLike(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            database.getInstance(context).userLikeDao().insert(UserLike(trip.tripid))
        }
    }*/

/*    @OptIn(DelicateCoroutinesApi::class)
    fun createDone(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            database.getInstance(context).userDoneDao().insert(UserDone(trip.tripid))
        }
    }*/

/*    @OptIn(DelicateCoroutinesApi::class)
    fun createTrip(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            database.getInstance(context).tripDao().insert(trip)
        }
    }*/

/*    @OptIn(DelicateCoroutinesApi::class)
    fun applyTrip(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            trip.pending = false
            database.getInstance(context).tripDao().update(trip)
        }
    }*/

/*    @OptIn(DelicateCoroutinesApi::class)
    fun declineTrip(trip: Trip, context: Context) {
        val database = AppDatabase
        GlobalScope.launch(Dispatchers.Main) {
            trip.pending = true
            database.getInstance(context).tripDao().update(trip)
        }
    }*/
