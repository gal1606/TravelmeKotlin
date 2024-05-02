package co.il.travelme.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import co.il.travelme.models.Trip
import com.example.travelme.extension.TripsSQLDBWorker

@Database(entities = [Trip::class], version = 1, exportSchema = false)
@TypeConverters(GeoPointConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tripDao(): TripDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "travelme-db")
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val request = OneTimeWorkRequestBuilder<TripsSQLDBWorker>().build()
                            WorkManager.getInstance(context).enqueue(request)
                        }
                    }
                )
                .build()
        }
    }
}
