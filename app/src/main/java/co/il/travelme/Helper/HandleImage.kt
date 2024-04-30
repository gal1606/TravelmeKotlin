package co.il.travelme.Helper


import android.net.Uri
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import co.il.travelme.`interface`.ImageHandlerCallback
import co.il.travelme.ui.AddTrip.AddTrip

class HandleImage(private val callback: ImageHandlerCallback, private val imageView: ImageView) {
    private lateinit var getContent: ActivityResultLauncher<String>


    fun registerImagePicker(fragment: Fragment) {
        getContent = fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                callback.onImageSelected(uri, imageView)
            } else {
                callback.onImageError("Failed to get image")
            }
        }
    }

    fun openGalleryForImage() {
        getContent.launch("image/*")
    }
}