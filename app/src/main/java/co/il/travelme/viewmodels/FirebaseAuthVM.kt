package co.il.travelme.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import co.il.travelme.Helper.bitmapToUrl
import co.il.travelme.models.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import java.io.ByteArrayOutputStream

class FirebaseAuthVM: ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth

    fun register(
        userEmail: String,
        userPassword: String,
        name: String,
        onSuccess: (result: User) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // כאן יש לבצע עדכון לפרופיל המשתמש
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }
                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // המשתמש נרשם בהצלחה, שלח את המשתמש ל-callback
                                onSuccess(
                                    User(
                                        id = auth.currentUser?.uid ?: "",
                                        email = auth.currentUser?.email ?: "",
                                        name = auth.currentUser?.displayName ?: "",
                                        profileImage = auth.currentUser?.photoUrl?.toString() ?: ""
                                    )
                                )
                            } else {
                                // העדכון נכשל, שלח את החריגה
                                onFailure(updateTask.exception ?: Exception("Failed to update user profile."))
                            }
                        }
                } else {
                    // הרישום נכשל, שלח את החריגה
                    onFailure(task.exception ?: Exception("Registration failed."))
                }
            }
    }

    fun login(userEmail:String, userPassword:String,
        onSuccess: (result: User) -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(
                        User(
                            id = auth.currentUser?.uid ?: "",
                            email = auth.currentUser?.email ?: "",
                            name = auth.currentUser?.displayName ?: "",
                            profileImage = auth.currentUser?.photoUrl.toString()
                        )
                    )
                } else {
                    onFailure(task.exception ?: Exception("Unknown exception."))
                }
            }
    }

}