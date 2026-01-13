package com.kwh.smartkabadiapp.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kwh.almuniconnect.MainActivity
import com.kwh.almuniconnect.R
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private const val CHANNEL_ID = "fcm_default_channel"
        private const val CHANNEL_NAME = "App Notifications"
        private const val CHANNEL_DESC = "General notifications from the app"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM Token: $token")
        // TODO: send token to your server if required
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message received: from=${remoteMessage.from}")

        // Log data to help debugging
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
        }
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message notification payload: ${remoteMessage.notification}")
        }

        val notification = remoteMessage.notification
        val title = notification?.title ?: remoteMessage.data["title"] ?: getString(R.string.app_name)
        val body = notification?.body ?: remoteMessage.data["body"] ?: ""
        val destination = remoteMessage.data["destination"] // e.g. "language"
        val data = remoteMessage.data // pass full map for image key etc.

        showNotification(title, body, destination, data)
    }

    /**
     * Build and show a local notification, with PendingIntent to open MainActivity.
     * This fetches an optional imageUrl from data["image"] and applies BigPictureStyle.
     */
    @SuppressLint("MissingPermission")
    private fun showNotification(
        title: String?,
        body: String?,
        destination: String?,
        data: Map<String, String>
    ) {
        // Build intent to open app -- customize to your navigation setup
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("from_notification", true)
            destination?.let { putExtra("destination", it) }
            // Use these flags so tapping from background/closed launches activity cleanly.
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.app_logo) // replace with your small icon
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Try to find image URL in data payload
        val imageUrl = data["image"] ?: data["image_url"] ?: data["banner"]

        var bitmap: Bitmap? = null
        if (!imageUrl.isNullOrEmpty()) {
            try {
                // onMessageReceived runs on a background thread, so blocking fetch is acceptable here
                bitmap = Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .submit()
                    .get()
                Log.d(TAG, "Fetched image for notification: $imageUrl")
            } catch (e: Exception) {
                Log.w(TAG, "Failed to fetch image for notification: $e")
                bitmap = null
            }
        }

        if (bitmap != null) {
            // Apply BigPictureStyle to show banner image when notification expands
            builder.setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap)
                // .bigLargeIcon(null) // avoids duplicating the large icon in expanded view
            )
            // Optional: set as large icon too (small round icon beside text)
            builder.setLargeIcon(bitmap)
        } else if (!body.isNullOrEmpty() && body.length > 40) {
            // fallback to BigTextStyle if we don't have an image
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }

        with(NotificationManagerCompat.from(this)) {
            notify(Random.nextInt(), builder.build())
        }
    }

    /**
     * Create notification channel for Android O+
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}