package co.il.travelme.Helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor
import java.io.IOException
import java.time.OffsetDateTime
import java.time.ZoneOffset



fun uriToBitmap(selectedFileUri: Uri, context: Context): Bitmap? {
    try {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(selectedFileUri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor!!.fileDescriptor
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}

fun bitmapToUrl(
    bitmap: Bitmap?,
    path: String,
    onSuccess: (result: Uri) -> Unit,
    onFailure: (exception: Exception) -> Unit) {

    val storageRef = FirebaseStorage.getInstance().reference

    if (bitmap != null) {
        val baos = ByteArrayOutputStream()
        val storagePictures = storageRef.child(path + OffsetDateTime.now(ZoneOffset.UTC))
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()
        val upload = storagePictures.putBytes(image)
        upload.addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                storagePictures.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        val imageUri = it
                        onSuccess(it)
                    }
                }
            }
            else {
                onFailure(uploadTask.exception ?: Exception("Unknown exception."))
            }
        }
    }
}


