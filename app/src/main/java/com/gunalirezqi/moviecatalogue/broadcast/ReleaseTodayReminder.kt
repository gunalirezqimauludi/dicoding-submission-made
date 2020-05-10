package com.gunalirezqi.moviecatalogue.broadcast

import android.annotation.TargetApi
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.api.TheMovieDBApi
import com.gunalirezqi.moviecatalogue.feature.main.MainActivity
import com.gunalirezqi.moviecatalogue.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ReleaseTodayReminder : BroadcastReceiver() {

    companion object {
        private const val ALARM_ID = 2
        private const val NOTIFICATION_ID = 2
        private const val CHANNEL_ID = "Channel_02"
        private const val CHANNEL_NAME = "ReleaseTodayReminderChannel"
    }

    @TargetApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val today = current.format(formatter)

        GlobalScope.launch(Dispatchers.Main) {
            val apiRepository = ApiRepository()
            val gson = Gson()

            val movie = gson.fromJson(
                apiRepository.doRequest(TheMovieDBApi.getMovieRelease(today)),
                MovieResponse::class.java
            )

            if (movie.results.isNotEmpty()) {
                for ((index, value) in movie.results.withIndex()) {
                    val title = value.movieTitle ?: ""
                    val message = "${value.movieTitle}, ${context.getString(R.string.release_reminder_message)}"
                    showReminderNotification(context, title, message, NOTIFICATION_ID + index)
                }
            }
        }
    }

    fun setReminder(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseTodayReminder::class.java)

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, 0)

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )

        Toast.makeText(context, context.getString(R.string.release_reminder_on), Toast.LENGTH_SHORT).show()
    }

    fun cancelReminder(context: Context?) {
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseTodayReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)

        Toast.makeText(context, context.getString(R.string.release_reminder_off), Toast.LENGTH_SHORT).show()
    }

    private fun showReminderNotification(
        context: Context,
        title: String,
        message: String,
        notifId: Int
    ) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            .setSound(alarmSound)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        mNotificationManager.notify(notifId, notification)
    }
}
