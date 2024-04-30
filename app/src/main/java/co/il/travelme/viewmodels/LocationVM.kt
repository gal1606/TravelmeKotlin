import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    val _locationData = MutableLiveData<LatLng>()

    // הוספת LiveData עבור זיהוי הצלחה/כישלון בקבלת הרשאה
    private val _isPermissionGranted = MutableLiveData<Boolean>()

    fun handlePermissions(permissions: Map<String, Boolean>) {
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                _isPermissionGranted.postValue(true)
                getLastLocation()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // ניתן לטפל במצב שרק הרשאת מיקום גס ניתנה
                _isPermissionGranted.postValue(false)
            }
            else -> {
                _isPermissionGranted.postValue(false)
            }
        }
    }

    fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(getApplication<Application>().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    _locationData.postValue(LatLng(location.latitude, location.longitude))
                } else {
                    // טיפול במקרה שהמיקום אינו זמין
                }
            }
        } else {
            // ההרשאה לא ניתנה, ניתן להציג הודעה או להפעיל תהליך בקשת הרשאה
        }
    }
}