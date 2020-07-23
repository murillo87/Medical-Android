package com.appamedix.makula.worker.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.worker.preference.InternalSettings
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    private val TAG = "AlarmReceiver"

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != null && context != null) {
            if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED, true)) {
                // Set the alarm after boot the device.
                Log.d(TAG, "onReceive: BOOT_COMPLETED")
                NotificationScheduler.setReminder(context, null)
                return
            }
        }

        Log.d(TAG, "onReceive: ")

        context?.let {
            // Trigger the notification.
            NotificationScheduler.showNotification(it)

            // Set new alarm for the next appointment.
            val curDate = Date()
            val focusDate = DateUtils.increaseMinutes(curDate, InternalSettings.reminderTime)
            NotificationScheduler.setReminder(it, focusDate)
        }
    }
}