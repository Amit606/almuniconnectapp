package com.kwh.almuniconnect.notification

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
        private const val CHANNEL_NAME = "Alumni Connect Notifications"
        private const val CHANNEL_DESC = "General notifications from the app"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM Token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val data = remoteMessage.data ?: emptyMap()

            val title = remoteMessage.notification?.title
                ?: data["title"]
                ?: getString(R.string.app_name)

            val body = remoteMessage.notification?.body
                ?: data["body"]
                ?: ""

            val destination = data["destination"]
            val ctaTitle = data["cta_title"]
            val ctaDestination = data["cta_destination"]

            showNotification(
                title = title,
                body = body,
                destination = destination,
                data = data,
                ctaTitle = ctaTitle,
                ctaDestination = ctaDestination
            )

        } catch (e: Exception) {
            Log.e(TAG, "Notification handling failed: ${e.message}")
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(
        title: String?,
        body: String?,
        destination: String?,
        data: Map<String, String>,
        ctaTitle: String?,
        ctaDestination: String?
    ) {

        try {

            // -------- Main Intent --------
            val mainIntent = Intent(this, MainActivity::class.java).apply {
                putExtra("from_notification", true)

                // Pass all data safely
                data.forEach { (key, value) ->
                    putExtra(key, value)
                }

                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

            val mainPendingIntent = PendingIntent.getActivity(
                this,
                Random.nextInt(),
                mainIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.app_logo)
                .setContentTitle(title ?: getString(R.string.app_name))
                .setContentText(body ?: "")
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(mainPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

            // -------- Handle Big Image (Optional) --------
            val imageUrl = data["image"] ?: data["image_url"] ?: data["banner"]

            var bitmap: Bitmap? = null

            if (!imageUrl.isNullOrEmpty()) {
                try {
                    bitmap = Glide.with(this)
                        .asBitmap()
                        .load(imageUrl)
                        .submit()
                        .get()
                } catch (e: Exception) {
                    Log.w(TAG, "Image load failed")
                }
            }

            if (bitmap != null) {
                builder.setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(bitmap)
                )
                builder.setLargeIcon(bitmap)
            } else if (!body.isNullOrEmpty()) {
                builder.setStyle(
                    NotificationCompat.BigTextStyle().bigText(body)
                )
            }

            // -------- Handle CTA (Optional) --------
            if (!ctaTitle.isNullOrEmpty() && !ctaDestination.isNullOrEmpty()) {

                val ctaIntent = Intent(this, MainActivity::class.java).apply {
                    putExtra("from_notification", true)
                    putExtra("destination", ctaDestination)

                    // Pass full data again
                    data.forEach { (key, value) ->
                        putExtra(key, value)
                    }

                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }

                val ctaPendingIntent = PendingIntent.getActivity(
                    this,
                    Random.nextInt(),
                    ctaIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                builder.addAction(
                    R.drawable.ic_google, // change icon if needed
                    ctaTitle,
                    ctaPendingIntent
                )
            }

            NotificationManagerCompat.from(this)
                .notify(Random.nextInt(), builder.build())

        } catch (e: Exception) {
            Log.e(TAG, "Show notification failed: ${e.message}")
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESC
                enableLights(true)
                enableVibration(true)
            }

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}
