package co.il.travelme.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.il.travelme.models.Trip
import co.il.travelme.models.UserDone
import co.il.travelme.models.UserLike
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class TripVM : ViewModel() {
    private val firebaseDBVM = FirebaseDBVM()  // ייבוא או יצירת מופע מ-FirebaseDBVM
    private val _trips = MutableLiveData<List<Trip>>()
    private var likeTrips = ArrayList<UserLike>()
    private var doneTrips = ArrayList<UserDone>()
    val trips: LiveData<List<Trip>> = _trips

    private val _filteMyTrips = MutableLiveData<List<Trip>>()
    val filterMyTrips: LiveData<List<Trip>> get() = _filteMyTrips
    private val _filteredTrips = MutableLiveData<List<Trip>>()
    val filterTrips: LiveData<List<Trip>> get() = _filteredTrips


    init {
        loadTrips()
    }

    private fun loadTrips() {
        if (_trips.value != null && _trips.value!!.isNotEmpty()) {
            return  // אם כבר טעונים, אל תטען שוב
        }
        firebaseDBVM.getTrips(
            onSuccess = { tripsList ->
                loadLikeTrips()
                loadDoneTrips()
                Log.i("gil","onSuccess")
                _trips.postValue(tripsList)
                Log.i("gil","onSuccess2" + tripsList.size)
            },
            onFailure = { exception ->
                Log.i("gil","onFailure")
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

    private fun loadLikeTrips() {
        firebaseDBVM.getLikedTrips(
            onSuccess = { tripsLikeList ->
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
    }

    fun filterTrips(difficulty: String, searchText: String) {
        _filteredTrips.value = _trips.value?.filter { trip ->
            trip.level == difficulty && trip.description.contains(searchText, ignoreCase = true)
        }
    }



}
