package co.il.travelme.ui.AddTrip

import android.R.attr.data
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import co.il.travelme.CurrentUser
import co.il.travelme.Helper.HandleImage
import co.il.travelme.R
import co.il.travelme.StoreTripVM.viewModel
import co.il.travelme.databinding.FragmentAddTripBinding
import co.il.travelme.interfaces.ImageHandlerCallback
import co.il.travelme.models.Trip
import com.bumptech.glide.Glide
import com.google.firebase.firestore.GeoPoint


class AddTrip : Fragment(), ImageHandlerCallback {
    private lateinit var binding: FragmentAddTripBinding
    private var selectedBitmap: Bitmap? = null
    private lateinit var mHandleImage : HandleImage
    private lateinit var trip : Trip

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTripBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHandleImage = HandleImage(this, binding.TripButton)  // נניח שהכפתור שלך נקרא tripButton
        mHandleImage.registerImagePicker(this)
        trip = Trip()

        binding.TripButton.setOnClickListener {
            openGalleryForImage()
        }

        binding.button.setOnClickListener {
            trip = PreparingTheTrip()
            viewModel.saveTripInDB(trip, requireActivity(), viewModel, selectedBitmap) {
                Toast.makeText(
                    activity, R.string.successfullyentered,
                    Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.itemFragment)
            }
        }

    }

    private fun PreparingTheTrip(): Trip {
        var tripPreparing = Trip()
        tripPreparing.time = binding.editTexttime.text.toString().toDouble()
        tripPreparing.imageUrl = selectedBitmap.toString()
        tripPreparing.length = binding.editTextLength.text.toString().toDouble()
        tripPreparing.description = binding.editTextTextMultiLine.text.toString()
        tripPreparing.title = binding.editTextTitle.text.toString()
        val latitude = binding.editTextlatitude.text.toString().toDouble()
        val longitude = binding.editTextlongitude.text.toString().toDouble()
        val coord = GeoPoint(latitude, longitude)
        tripPreparing.coord = coord
        tripPreparing.level = binding.spinner.getSelectedItem().toString()
        tripPreparing.UserId = CurrentUser.currentUser.id
        return tripPreparing
    }

    private fun openGalleryForImage() {
        mHandleImage.openGalleryForImage()
    }
    override fun onImageSelected(imageUri: Uri, imageView: ImageView) {
        Glide.with(this)
            .load(imageUri)
            .into(imageView)

        selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imageUri)
    }

    override fun onImageError(error: String) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

//    fun saveTripInDB(trip: Trip, context: Context, viewModel: TripVM) {
//        bitmapToUrl(
//            bitmap = selectedBitmap,
//            path = "trips/",
//            onSuccess = { result ->
//                trip.imageUrl = result.toString()
//                StoreViewModel.storeViewModel.addTrip(
//                    trip = trip,
//                    onSuccess = {
//                       /* viewModel.createTrip(trip, context).apply {
//                            navController.navigate(Graph.MYTRIPS)
//                        }*/
//                    },
//                    onFailure = {}
//                )
//            },
//            onFailure = {}
//        )
//    }

}