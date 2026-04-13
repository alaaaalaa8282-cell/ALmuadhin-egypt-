package com.example.almuadhin.data.repo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val fused = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(): android.location.Location? {
        return try {
            val task = fused.lastLocation
            val last = suspendCancellableCoroutine<android.location.Location?> { cont ->
                task.addOnSuccessListener { location ->
                    cont.resume(location)
                }
                task.addOnFailureListener {
                    cont.resume(null)
                }
            }
            if (last != null) last else getCurrentLocation()
        } catch (e: Exception) {
            getCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getCurrentLocation(): android.location.Location? =
        suspendCancellableCoroutine { cont ->
            val request = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 5000L
            )
                .setMaxUpdates(1)
                .setMinUpdateDistanceMeters(0f)
                .setWaitForAccurateLocation(true)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    fused.removeLocationUpdates(this)
                    cont.resume(result.lastLocation)
                }
            }

            fused.requestLocationUpdates(request, callback, Looper.getMainLooper())

            cont.invokeOnCancellation {
                fused.removeLocationUpdates(callback)
            }
        }

}
