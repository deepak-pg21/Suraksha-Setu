package com.mindmatrix.surakshasetu.geofence

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class GeofenceHelper(val context: Context) {

    private val geofencingClient: GeofencingClient =
        LocationServices.getGeofencingClient(context)

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(geofence)
        }.build()
    }

    @SuppressLint("MissingPermission")
    fun addGeofence(latitude: Double, longitude: Double, radius: Float) {
        val geofence = createGeofence(latitude, longitude, radius)
        val request = getGeofencingRequest(geofence)

        geofencingClient.addGeofences(request, geofencePendingIntent).run {
            addOnSuccessListener {
                // Geofence added successfully
            }
            addOnFailureListener {
                // Failed to add geofence
            }
        }
    }

    fun removeGeofences() {
        geofencingClient.removeGeofences(geofencePendingIntent).run {
            addOnSuccessListener {
                // Geofences removed
            }
            addOnFailureListener {
                // Failed to remove
            }
        }
    }

    private fun createGeofence(
        latitude: Double,
        longitude: Double,
        radius: Float
    ): Geofence {
        return Geofence.Builder()
            .setRequestId("SOS_ZONE")
            .setCircularRegion(latitude, longitude, radius)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(
                Geofence.GEOFENCE_TRANSITION_ENTER or
                        Geofence.GEOFENCE_TRANSITION_EXIT
            )
            .build()
    }
}
