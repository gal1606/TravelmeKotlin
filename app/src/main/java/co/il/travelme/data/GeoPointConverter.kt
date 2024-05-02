package co.il.travelme.data
import androidx.room.TypeConverter
import com.google.firebase.firestore.GeoPoint

class GeoPointConverter {
    @TypeConverter
    fun fromGeoPoint(geoPoint: GeoPoint?): String? {
        return geoPoint?.let { "${it.latitude},${it.longitude}" }
    }

    @TypeConverter
    fun toGeoPoint(value: String?): GeoPoint? {
        return value?.let {
            val coords = it.split(",")
            GeoPoint(coords[0].toDouble(), coords[1].toDouble())
        }
    }
}