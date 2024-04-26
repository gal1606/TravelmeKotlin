package co.il.travelme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.il.travelme.models.User
import co.il.travelme.viewmodels.FirebaseAuthVM
import co.il.travelme.viewmodels.FirebaseDBVM

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

object CurrentUser {
    lateinit var currentUser: User
}
object AuthViewModel {
    var authViewModel: FirebaseAuthVM = FirebaseAuthVM()
}
object StoreViewModel {
    var storeViewModel: FirebaseDBVM = FirebaseDBVM()
}

object DialogMessage {
    var dialogMessage : String = ""
}

