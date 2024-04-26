package co.il.travelme.viewmodels

import androidx.lifecycle.ViewModel
import co.il.travelme.CurrentUser
import co.il.travelme.models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseDBVM: ViewModel() {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val dbUsers: CollectionReference = db.collection("Users")


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

}


