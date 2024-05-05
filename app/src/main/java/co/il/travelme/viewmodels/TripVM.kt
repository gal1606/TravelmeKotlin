package co.il.travelme.viewmodels

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import co.il.travelme.Helper.bitmapToUrl
import co.il.travelme.StoreViewModel
import co.il.travelme.data.AppDatabase
import co.il.travelme.data.TripDao
import co.il.travelme.models.Trip
import co.il.travelme.models.UserDone
import co.il.travelme.models.UserLike
import kotlinx.coroutines.launch


class TripVM (application: Application) : AndroidViewModel(application) {
    private val firebaseDBVM = FirebaseDBVM()  // ייבוא או יצירת מופע מ-FirebaseDBVM
    private var likeTrips = ArrayList<UserLike>()
    private var doneTrips = ArrayList<UserDone>()
    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips

    private val _filteMyTrips = MutableLiveData<List<Trip>>()
    val filterMyTrips: LiveData<List<Trip>> get() = _filteMyTrips
    private val _filteredTrips = MutableLiveData<List<Trip>>()
    val filterTrips: LiveData<List<Trip>> get() = _filteredTrips
    private val db: AppDatabase = AppDatabase.getInstance(application)
    private val tripDao: TripDao =db.tripDao()


    init {
        loadTrips()
    }

    // בפונקציה לטעינת טיולים
    private fun loadTrips() {
        viewModelScope.launch {
            try {
                // קודם כל בודק אם יש נתונים ב-ROOM
                val localTrips = tripDao.getAllTrips()  // קריאה אסינכרונית ל-ROOM
                if (localTrips.isNotEmpty()) {
                    // יש נתונים במקומי, אז משתמשים בהם
                    _trips.postValue(localTrips)
                } else {
                    loadFromFirebase()
                }
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error loading trips from database", e)
                loadFromFirebase()
            }
        }
    }

    private fun loadFromFirebase() {
        firebaseDBVM.getTrips(
            onSuccess = { tripsList ->
                loadLikeTrips()
                Log.i("gil", "onSuccess")
                _trips.postValue(tripsList)
                Log.i("gil", "onSuccess2" + tripsList.size)
            },
            onFailure = { exception ->
                Log.i("gil", "onFailure")
            }
        )
    }

    private fun loadDoneTrips() {
        firebaseDBVM.getDoneTrips(
            onSuccess = { tripsDoneList ->
                Log.i("gil","onSuccess5")
                doneTrips = tripsDoneList
                filterIsDone()
            },
            onFailure = {
                Log.i("gil","onFailure6")
            }
        )
    }

    private fun saveTripsInRoom(tripsList: List<Trip>) {
        viewModelScope.launch {
            tripDao.upsertAll(tripsList)
        }
    }


    private fun loadLikeTrips() {
        firebaseDBVM.getLikedTrips(
            onSuccess = { tripsLikeList ->
                loadDoneTrips()
                Log.i("gil","onSuccess3")
                likeTrips = tripsLikeList
                filterIsLike()
            },
            onFailure = {
                Log.i("gil","onFailure4")
            }
        )
    }

    fun searchMyTrips(id: String) {
        _filteMyTrips.value = _trips.value?.filter { trip ->
            trip.UserId == id
        }
    }

    private fun filterIsLike() {
        val likedTripIds = likeTrips.map { it.tripid }.toSet()  // שימוש ב-Set לחיפוש מהיר
        _trips.value?.forEach { trip ->
            trip.isLike = likedTripIds.contains(trip.id)  // בדיקה מהירה אם ה-ID נמצא ב-Set
        }
        _trips.postValue(_trips.value)  // עדכון ה-LiveData כדי שהשינויים ישקפו ב-UI
    }

    private fun filterIsDone() {
        val doneTripIds = doneTrips.map { it.tripid }.toSet()  // שימוש ב-Set לחיפוש מהיר
        _trips.value?.forEach { trip ->
            trip.isDone = doneTripIds.contains(trip.id)  // בדיקה מהירה אם ה-ID נמצא ב-Set
        }
        _trips.postValue(_trips.value)  // עדכון ה-LiveData כדי שהשינויים ישקפו ב-UI
        _trips.value?.let { saveTripsInRoom(it) }
    }

    fun filterTrips(difficulty: String, searchText: String) {
        _filteredTrips.value = _trips.value?.filter { trip ->
            trip.level == difficulty && trip.description.contains(searchText, ignoreCase = true)
        }
    }

    fun saveTripInDB(trip: Trip, context: Context, viewModel: TripVM, selectedBitmap: Bitmap?, onCompletion: () -> Unit) {
        bitmapToUrl(
            bitmap = selectedBitmap,
            path = "trips/",
            onSuccess = { imageUrl ->
                trip.imageUrl = imageUrl.toString()
                StoreViewModel.storeViewModel.addTrip(
                    trip = trip,
                    onSuccess = { trip ->
                        Log.i("gil", trip.tripid)
                        viewModel.viewModelScope.launch {
                            tripDao.insert(trip)
                            val currentList = _trips.value ?: emptyList()
                            val updatedList = currentList + trip
                            _trips.value = updatedList
                            onCompletion() // קריאה לפונקציה לאחר שהכל הסתיים
                        }
                    },
                    onFailure = {
                        onCompletion() // קריאה לפונקציה גם אם יש כישלון
                    }
                )
            },
            onFailure = {
                onCompletion() // קריאה לפונקציה גם אם יש כישלון בהעלאת התמונה
            }
        )
    }

}
