package co.il.travelme.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.android.gms.maps.model.LatLng
import androidx.room.PrimaryKey
import com.google.firebase.firestore.GeoPoint


data class Trip(
    var id: String = "",  // הוספת שדה id כדי להתאים למבנה ב-Firebase
    var description: String = "",
    var title : String = "",
    var coord: GeoPoint = GeoPoint(0.0, 0.0),  // וודא שהטיפוס הזה מופיע כך בפועל במסד הנתונים
    var level: String = "",
    var imageUrl: String = "",
    var length: Double = 0.0,
    var time: Double = 0.0,
    var pending: Boolean = false,
    var tripid: String = "",
    var UserId: String = ""
) {
    constructor() : this("", "","", GeoPoint(0.0, 0.0), "", "", 0.0, 0.0, false,"","")
}