package co.il.travelme.ui.Profile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.il.travelme.AuthViewModel.authViewModel
import co.il.travelme.CurrentUser
import co.il.travelme.Helper.HandleImage
import co.il.travelme.R
import co.il.travelme.StoreTripVM.viewModel
import co.il.travelme.StoreViewModel
import co.il.travelme.databinding.FragmentProfileBinding
import co.il.travelme.`interface`.ImageHandlerCallback
import co.il.travelme.models.Trip
import co.il.travelme.ui.home.MyItemRecyclerViewAdapter
import co.il.travelme.ui.login.LoginActivity
import com.bumptech.glide.Glide

class ProfileFragment : Fragment(), ImageHandlerCallback {
    private lateinit var adapter: MyItemRecyclerViewAdapter

    private lateinit var tripsObserver: Observer<List<Trip>>

    private var selectedBitmap: Bitmap? = null
    private lateinit var binding: FragmentProfileBinding
    private lateinit var mHandleImage : HandleImage


        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentProfileBinding.inflate(inflater, container, false)
            return binding.getRoot()
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            fillingDetails()
            mHandleImage = HandleImage(this, binding.profileButton)  // נניח שהכפתור שלך נקרא tripButton
            mHandleImage.registerImagePicker(this)

            binding.profileButton.setOnClickListener {
                openGalleryForImage()
            }

            binding.logoutbutton.setOnClickListener {
                authViewModel.logout(
                    onSuccess = {
                        val intent = Intent(activity, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity?.finish()
                    },
                    onFailure = { exception ->
                        Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show()
                    })
            }

            binding.AddNewTripButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_addTrip)
            }

            adapter = MyItemRecyclerViewAdapter()

            binding.list.layoutManager = LinearLayoutManager(context)
            binding.list.adapter = adapter

            viewModel.filteredTrips.observe(viewLifecycleOwner, Observer { trips ->
                Log.i("gil", "trip size: " + trips.size)
                adapter.submitList(trips)
            })

            viewModel.searchMyTrips(CurrentUser.currentUser.id)

            binding.save.setOnClickListener {
                saveProfile()
            }
        }

    private fun fillingDetails() {
        binding.textViewEmail.text = CurrentUser.currentUser.email
        binding.NameEditText.setText(CurrentUser.currentUser.name)
        activity?.let {
            Glide.with(it.applicationContext) // זה ישתמש ב-context של ה-view של ה-ViewHolder
                .load(CurrentUser.currentUser.profileImage) // ודא שאתה מעביר את URL של התמונה מהאובייקט `item`
                .placeholder(R.drawable.user_solid) // תמונת טעינה זמנית
                .error(R.drawable.user_solid) // תמונה במקרה של שגיאת טעינה
                .centerCrop() // חיתוך התמונה כדי להתאים ל-ImageView
                .into(binding.profileButton)
        }
    }


    private fun saveProfile() {
        authViewModel.updateProfile(selectedBitmap,binding.NameEditText.text.toString(),
            onSuccess = { user ->
                CurrentUser.currentUser.profileImage = user.profileImage
                CurrentUser.currentUser.name = user.name
                CurrentUser.currentUser.let {
                    StoreViewModel.storeViewModel.updateUser(
                        user = it,
                        onSuccess = {
                        },
                        onFailure = {}
                    )
                }
        },
            onFailure = { exception ->
                Toast.makeText(activity, exception.toString(), Toast.LENGTH_LONG).show()
            })
    }


    private fun openGalleryForImage() {
        mHandleImage.openGalleryForImage()
    }
    override fun onImageSelected(imageUri: Uri, imageView: ImageView) {
        Glide.with(this)
            .load(imageUri)
            .into(imageView)

        val selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
    }

    override fun onImageError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

}
