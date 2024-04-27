package co.il.travelme.ui.Profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import co.il.travelme.AuthViewModel.authViewModel
import co.il.travelme.CurrentUser
import co.il.travelme.MainActivity
import co.il.travelme.StoreViewModel
import co.il.travelme.databinding.FragmentProfileBinding
import co.il.travelme.ui.login.LoginActivity
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutbutton.setOnClickListener {
            authViewModel.logout(
                onSuccess = {
                    val intent = Intent(activity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()
                },
                onFailure = { exception ->
                    Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show()
                })
        }
    }
}