package co.il.travelme.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.il.travelme.CurrentUser
import co.il.travelme.models.Trip
import co.il.travelme.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

class FirebaseDBVM: ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val dbUsers: CollectionReference = db.collection("Users")
    private val dbTrips: CollectionReference = db.collection("Trips")
    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips


    fun addUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbUsers.add(user)
            .addOnSuccessListener {
                CurrentUser.currentUser.id = it.id
                dbUsers.document(CurrentUser.currentUser.id).update("id", CurrentUser.currentUser.id).addOnSuccessListener {
                    onSuccess()
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun getTrips(
        onSuccess: (result: ArrayList<Trip>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        Log.i("gil","document")
        val tripsList: ArrayList<Trip> = arrayListOf()
        dbTrips.get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.i("gil",document.id)
                val tripId = document.id
                val description = document.getString("description") ?: ""
                val imageUrl = document.getString("imageUrl") ?: ""
                val level = document.getString("level") ?: ""
                val length = document.getDouble("length") ?: 0.0
                val time = document.getDouble("time") ?: 0.0
                val pending = document.getBoolean("pending") ?: false
                val geoPoint = document.getGeoPoint("coord")

                if (geoPoint != null) {
                    val trip = Trip(
                        id = tripId,
                        description = description,
                        coord = geoPoint,
                        level = level,
                        imageUrl = imageUrl,
                        length = length,
                        time = time,
                        pending = pending
                    )
                    tripsList.add(trip)
                }
            }
            Log.i("gil","finish")
            onSuccess(tripsList)
        }.addOnFailureListener { exception ->
            // Log the error or handle the failure
        }
    }

}


