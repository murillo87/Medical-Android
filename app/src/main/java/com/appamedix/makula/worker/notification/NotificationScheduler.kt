package com.appamedix.makula.worker.notification

import android.annotation.SuppressLint
import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.appamedix.makula.BuildConfig
import com.appamedix.makula.R
import com.appamedix.makula.scenes.splash.SplashSceneActivity
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.worker.datamodel.appointment.AppointmentObject
import com.appamedix.makula.worker.datamodel.appointment.getAppointmentsAfter
import com.appamedix.makula.worker.preference.InternalSettings
import io.realm.Realm
import java.util.*

object NotificationScheduler {

    // The notification id.
    private const val REMINDER_REQUEST_CODE: Int = 100
    // The notification object.
    private lateinit var notification: Notification
    private var notificationManager: NotificationManager? = null

    private const val CHANNEL_ID = BuildConfig.APPLICATION_ID + "CHANNEL_ID"
    private const val CHANNEL_NAME = "Makula Reminder"

    // The appointment object.
    private var appointment: AppointmentObject = AppointmentObject()

    /**
     * Creates a notification channel.
     *
     * @param context: The application context.
     */
    @SuppressLint("NewApi")
    private fun createChannel(context: Context) {
        if (notificationManager == null) {
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Creates the NotificationChannel, but only on API 26+,
            // because the NotificationChannel class is new and not in the support library.
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableVibration(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = ContextCompat.getColor(context, R.color.colorNotification)
            notificationChannel.description = context.getString(R.string.localNotificationChannel)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Deletes old notification and sets new one.
     *
     * @param context: The application context.
     * @param date: The focus date to retrieve appointments.
     */
    fun setReminder(context: Context, date: Date?) {
        val realm = Realm.getDefaultInstance()

        // Cancel already scheduled reminders.
        cancelReminder(context)

        // Skip if user didn't activate reminders.
        if (!InternalSettings.reminderOn) return

        // Retrieve all future appointments.
        val focusDate = date ?: Date()
        var appointments = realm.getAppointmentsAfter(focusDate) ?: return
        appointment = appointments.first() ?: return

        val calendar = Calendar.getInstance()
        var notifyTime = DateUtils.increaseMinutes(appointment.getAppointmentDate(), -InternalSettings.reminderTime)

        if (notifyTime.before(focusDate) || (notifyTime == focusDate)) {
            // If the remaining time to upcoming appointment is less than reminder interval,
            // gets next appointment in reminder interval later.
            val nextDate = DateUtils.increaseMinutes(focusDate, InternalSettings.reminderTime)
            appointments = realm.getAppointmentsAfter(nextDate) ?: return
            appointment = appointments.first() ?: return
            notifyTime = DateUtils.increaseMinutes(appointment.getAppointmentDate(), -InternalSettings.reminderTime)
        }

        calendar.time = notifyTime
        realm.close()

        // Enable a receiver.
        val receiver = ComponentName(context, AlarmReceiver::class.java)
        val packageManager = context.packageManager
        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)

        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
                REMINDER_REQUEST_CODE,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= 23) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }
    }

    /**
     * Cancels any pending notifications. Should be called before creating a new notification.
     *
     * @param context: The application context.
     */
    private fun cancelReminder(context: Context) {
        // Disable a receiver.
        val receiver = ComponentName(context, AlarmReceiver::class.java)
        val packageManager = context.packageManager
        packageManager.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)

        val alarmManager = context.getSystemService(Activity.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,
                REMINDER_REQUEST_CODE,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    /**
     * Shows a notification that is available.
     *
     * @param context: The application context.
     */
    fun showNotification(context: Context) {
        // Create channel.
        createChannel(context)

        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationIntent = Intent(context, SplashSceneActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(SplashSceneActivity::class.java)
        stackBuilder.addNextIntent(notificationIntent)

        val title = context.getString(appointment.getAppointmentType().nameString())
        val formatString = context.getString(R.string.commonTimeFormat)
        val dateString = DateUtils.toSimpleString(appointment.getAppointmentDate(), formatString)
        val content = String.format(
                context.getString(R.string.localNotificationMessage),
                dateString
        )

        val pendingIntent = stackBuilder.getPendingIntent(REMINDER_REQUEST_CODE,
                PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
                    .setAutoCancel(true)
                    .build()
        } else {
            notification = NotificationCompat.Builder(context)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_round))
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build()
        }

        notificationManager?.notify(REMINDER_REQUEST_CODE, notification)
    }
}