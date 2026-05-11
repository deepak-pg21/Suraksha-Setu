package com.mindmatrix.surakshasetu.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.media.MediaRecorder
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mindmatrix.surakshasetu.R
import com.mindmatrix.surakshasetu.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import kotlin.math.sqrt

class EmergencyShakeService : Service(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    
    private var lastUpdate: Long = 0
    private var lastX: Float = 0.0f
    private var lastY: Float = 0.0f
    private var lastZ: Float = 0.0f
    private val shakeThreshold = 800

    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private var ringtone: Ringtone? = null

    companion object {
        private const val CHANNEL_ID = "emergency_shake_channel"
        private const val NOTIFICATION_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification("Service Active", "Shake detection is monitoring for your safety"))

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "Emergency Shake Service Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }

    private fun createNotification(title: String, text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "ACTION_TRIGGER_SOS") {
            triggerEmergency()
        }
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            val curTime = System.currentTimeMillis()
            if ((curTime - lastUpdate) > 100) {
                val diffTime = curTime - lastUpdate
                lastUpdate = curTime

                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                val speed = sqrt(
                    ( (x - lastX) * (x - lastX) + (y - lastY) * (y - lastY) + (z - lastZ) * (z - lastZ) ).toDouble()
                ) / diffTime * 10000

                if (speed > shakeThreshold) {
                    triggerEmergency()
                }

                lastX = x
                lastY = y
                lastZ = z
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun triggerEmergency() {
        Log.d("SOS", "Emergency Triggered!")
        updateNotification("SOS TRIGGERED!", "Fetching location and sending alerts...")
        
        // Play local alarm sound
        try {
            val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            ringtone = RingtoneManager.getRingtone(applicationContext, alarmUri)
            ringtone?.play()
        } catch (e: Exception) {
            Log.e("SOS", "Failed to play alarm", e)
        }

        val priority = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
        fusedLocationClient.getCurrentLocation(priority, null).addOnSuccessListener { location: Location? ->
            if (location != null) {
                Log.d("SOS", "Location: ${location.latitude}, ${location.longitude}")
                sendAlerts(location)
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                    lastLoc?.let { sendAlerts(it) }
                }
            }
        }

        startRecording()
    }

    private fun sendAlerts(location: Location) {
        val message = "EMERGENCY! I need help. My location: https://www.google.com/maps/search/?api=1&query=${location.latitude},${location.longitude}"
        
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = AppDatabase.getDatabase(applicationContext).contactDao().getAllContacts().first()
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getSystemService(SmsManager::class.java)
            } else {
                @Suppress("DEPRECATION")
                SmsManager.getDefault()
            }
            
            for (contact in contacts) {
                try {
                    smsManager.sendTextMessage(contact.phoneNumber, null, message, null, null)
                    Log.d("SOS", "SMS sent to ${contact.name}")
                } catch (e: Exception) {
                    Log.e("SOS", "Failed to send SMS to ${contact.name}", e)
                }
            }
        }
    }

    private fun updateNotification(title: String, text: String) {
        val notification = createNotification(title, text)
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }

    private fun startRecording() {
        if (isRecording) return
        
        val outFile = File(getExternalFilesDir(null), "sos_audio_${System.currentTimeMillis()}.mp3")
        
        mediaRecorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(this)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outFile.absolutePath)
            try {
                prepare()
                start()
                isRecording = true
                Log.d("SOS", "Recording started: ${outFile.absolutePath}")
            } catch (e: IOException) {
                Log.e("SOS", "prepare() failed", e)
            }
        }

        // Stop recording after 30 seconds
        android.os.Handler(mainLooper).postDelayed({
            stopRecording()
        }, 30000)
    }

    private fun stopRecording() {
        if (!isRecording) return
        try {
            mediaRecorder?.stop()
        } catch (e: Exception) {
            Log.e("SOS", "stop failed", e)
        } finally {
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false
        }
        
        // Stop local alarm when recording stops (or you can keep it longer)
        ringtone?.stop()

        Log.d("SOS", "Recording stopped")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        stopRecording()
    }
}
