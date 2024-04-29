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
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_profileFragment_to_itemFragment)
        }

        mainBinding.bottomBar.profile.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_itemFragment_to_profileFragment2)
        }

        mainBinding.bottomBar.search.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.action_itemFragment_to_searchFragment)
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

