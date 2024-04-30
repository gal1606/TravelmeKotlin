package co.il.travelme.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.il.travelme.models.Trip


class TripVM : ViewModel() {
    private val firebaseDBVM = FirebaseDBVM()  // ייבוא או יצירת מופע מ-FirebaseDBVM
    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips

    private val _filteMyTrips = MutableLiveData<List<Trip>>()
    val filterMyTrips: LiveData<List<Trip>> get() = _filteMyTrips
    private val _filteredTrips = MutableLiveData<List<Trip>>()
    val filterTrips: LiveData<List<Trip>> get() = _filteredTrips


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

    fun searchMyTrips(id: String) {
        _filteMyTrips.value = _trips.value?.filter { trip ->
            trip.UserId == id
        }
    }

    fun filterTrips(difficulty: String, searchText: String) {
        _filteredTrips.value = _trips.value?.filter { trip ->
            trip.level == difficulty && trip.description.contains(searchText, ignoreCase = true)
        }
    }


}