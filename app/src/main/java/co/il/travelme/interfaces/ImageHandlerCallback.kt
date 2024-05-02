package co.il.travelme.interfaces

import android.net.Uri
import android.widget.ImageView

interface ImageHandlerCallback {
    fun onImageSelected(imageUri: Uri, imageView: ImageView)
    fun onImageError(error: String)
}
