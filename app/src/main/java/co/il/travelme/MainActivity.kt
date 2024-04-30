package co.il.travelme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import co.il.travelme.databinding.ActivityMainBinding
import co.il.travelme.models.User
import co.il.travelme.viewmodels.FirebaseAuthVM
import co.il.travelme.viewmodels.FirebaseDBVM
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import co.il.travelme.viewmodels.TripVM

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root

        mainBinding.bottomBar.home.setOnClickListener {
            // בדיקה אם המשתמש כבר נמצא בעמוד הבית
            if (findNavController(R.id.nav_host_fragment).currentDestination?.id != R.id.itemFragment) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.itemFragment)
            }
        }

        mainBinding.bottomBar.profile.setOnClickListener {
            // בדיקה אם המשתמש כבר נמצא בעמוד הפרופיל
            if (findNavController(R.id.nav_host_fragment).currentDestination?.id != R.id.profileFragment) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.profileFragment)
            }
        }

        mainBinding.bottomBar.search.setOnClickListener {
            // בדיקה אם המשתמש כבר נמצא בעמוד החיפוש
            if (findNavController(R.id.nav_host_fragment).currentDestination?.id != R.id.searchFragment) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.searchFragment)
            }
        }
        setContentView(view)
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
object StoreTripVM {
    lateinit var viewModel: TripVM
}

