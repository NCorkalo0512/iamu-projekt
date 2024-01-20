package hr.algebra.imdbmovies

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import hr.algebra.imdbmovies.framework.setBooleanPreference
import hr.algebra.imdbmovies.framework.startActivity
import android.util.Log
import android.widget.RemoteViews

class MovieReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "AlarmChannel"
    private val NOTIFICATION_ID = 123

    override fun onReceive(context: Context, intent: Intent) {


        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Provjeriti je li postoji kanal
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val existingChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (existingChannel == null) {
                // Ako nema kreiraj novi
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Alarm Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    enableLights(true)
                    lightColor = Color.RED
                    enableVibration(true)
                    vibrationPattern = longArrayOf(0, 500, 500, 500, 500)
                }
                notificationManager.createNotificationChannel(channel)
            }
        }
        val notificationView = RemoteViews(context.packageName, R.layout.notification)
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Alarm Activated")
            .setContentText("Your alarm has been triggered.")
            .setColor(ContextCompat.getColor(context, R.color.notification_color))
            .setAutoCancel(true)
            .setContent(notificationView)
        // Prikazati obavijest
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}