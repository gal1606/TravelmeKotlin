package com.example.travelme.extension

/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import co.il.travelme.StoreViewModel
import co.il.travelme.data.AppDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.suspendCoroutine

class TripsSQLDBWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SuspiciousIndentation")
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getInstance(applicationContext)
             StoreViewModel.storeViewModel.getTrips(
                 onSuccess = { resultTrip ->
//                     StoreViewModel.storeViewModel.getLikedTrips(
//                         onSuccess = { resultLike ->
//                             StoreViewModel.storeViewModel.getDoneTrips(
//                                 onSuccess = { resultDone ->
//                                     GlobalScope.launch(Dispatchers.Main) {
//                                         database.tripDao().upsertAll(resultTrip)
//                                         database.userLikeDao().upsertAll(resultLike)
//                                         database.userDoneDao().upsertAll(resultDone)
//                                     }
//                                 },
//                                 onFailure = {}
//                             )
//                         },
//                         onFailure = {}
//                     )
                 },
                 onFailure = {
                     Result.failure()
                 }
             ).run {
                 Result.success()
             }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "TripsSQLDBWorker"
        const val KEY_FILENAME = "TRIP_DATA_FILENAME"
    }
}
