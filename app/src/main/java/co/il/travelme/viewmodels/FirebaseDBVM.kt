package co.il.travelme.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.il.travelme.CurrentUser
import co.il.travelme.models.Trip
import co.il.travelme.models.User
import co.il.travelme.models.UserDone
import co.il.travelme.models.UserLike
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

class FirebaseDBVM: ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val dbUsers: CollectionReference = db.collection("Users")
    private val dbTrips: CollectionReference = db.collection("Trips")
    private val dbLike: CollectionReference = db.collection("Like")
    private val dbDone: CollectionReference = db.collection("Done")

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> = _trips


    fun addTrip(
        trip: Trip,
        onSuccess: (result: Trip) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbTrips.add(trip)
            .addOnSuccessListener {
                trip.tripid = it.id
                dbTrips.document(trip.tripid).update("id", trip.tripid).addOnSuccessListener {
                    onSuccess(trip)
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

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

    fun like(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val data = hashMapOf("tripId" to tripId)

        dbLike.document(CurrentUser.currentUser.id).collection("like").document(tripId)
            .set(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("gil", "like")
                    onSuccess()
                } else {
                    task.exception?.let {
                        Log.e("gil", "Error liking trip: ", it)
                        onFailure(it)
                    }
                }
            }
    }

    fun unlike(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbLike.document(CurrentUser.currentUser.id)
            .collection("like")
            .document(tripId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("gil", "Unlike successful")
                    onSuccess()
                } else {
                    task.exception?.let {
                        Log.e("gil", "Error unliking trip: ", it)
                        onFailure(it)
                    }
                }
            }
    }

    fun getLikedTrips(
        onSuccess: (result: ArrayList<UserLike>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripIdsList: ArrayList<UserLike> = arrayListOf()

        dbLike.document(CurrentUser.currentUser.id).collection("like").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tripIdsList.add(UserLike(tripid=document.id))
                    Log.i("gil","Like" + document.id)
                }
                onSuccess(tripIdsList)
            }
    }


    fun getDoneTrips(
        onSuccess: (result: ArrayList<UserDone>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripIdsList: ArrayList<UserDone> = arrayListOf()
        dbDone.document(CurrentUser.currentUser.id).collection("done").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    tripIdsList.add(UserDone(tripid=document.id))
                }
                onSuccess(tripIdsList)
            }
    }


    fun updateUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbUsers.document(CurrentUser.currentUser.id).get()
            .addOnSuccessListener {
                val update: MutableMap<String, String> = HashMap()
                update["name"] = user.name
                update["profileImage"] = user.profileImage
                update["email"] = user.email
                update["id"] = user.id
                CurrentUser.currentUser = user
                dbUsers.document(CurrentUser.currentUser.id).set(update).addOnCompleteListener {
                    onSuccess()
                }
            }.addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun markAsDone(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val data = hashMapOf("tripId" to tripId, "done" to true)

        dbDone.document(CurrentUser.currentUser.id).collection("done").document(tripId)
            .set(data)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("gil", "Trip marked as done")
                    onSuccess()
                } else {
                    task.exception?.let {
                        Log.e("gil", "Error marking trip as done: ", it)
                        onFailure(it)
                    }
                }
            }
    }

    fun unmarkAsDone(
        tripId: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        dbDone.document(CurrentUser.currentUser.id).collection("done").document(tripId)
            .delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i("gil", "Trip unmarked as done")
                    onSuccess()
                } else {
                    task.exception?.let {
                        Log.e("gil", "Error unmarking trip as done: ", it)
                        onFailure(it)
                    }
                }
            }
    }

    fun getTrips(
        onSuccess: (result: ArrayList<Trip>) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val tripsList: ArrayList<Trip> = arrayListOf()
        dbTrips.get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.i("gil",document.id)
                val tripId = document.id
                val description = document.getString("description") ?: ""
                val title = document.getString("title") ?: ""
                val imageUrl = document.getString("imageUrl") ?: ""
                val level = document.getString("level") ?: ""
                val length = document.getDouble("length") ?: 0.0
                val time = document.getDouble("time") ?: 0.0
                val pending = document.getBoolean("pending") ?: false
                val geoPoint = document.getGeoPoint("coord")
                val userId = document.getString("userId") ?: ""


                if (geoPoint != null) {
                    val trip = Trip(
                        id = tripId,
                        description = description,
                        title = title,
                        coord = geoPoint,
                        level = level,
                        imageUrl = imageUrl,
                        length = length,
                        time = time,
                        pending = pending,
                        UserId = userId
                    )
                    tripsList.add(trip)
                }
            }
            onSuccess(tripsList)
        }.addOnFailureListener { exception ->
            // Log the error or handle the failure
        }
    }

}


