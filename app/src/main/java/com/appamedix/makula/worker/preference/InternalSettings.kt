package com.appamedix.makula.worker.preference

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.appamedix.makula.constants.Const

object InternalSettings {
    private const val TAG = "SETTINGS"
    private const val NAME = "MakulaApp"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // The app preference for the versioning the settings.
    private val SETTINGS_VERSION_PREF = Pair("SettingsPreferenceKeySettingsVersion", 0)

    // The app preference which indicates whether the disclaimer has been accepted by the user.
    private val DISCLAIMER_ACCEPT_PREF = Pair("SettingsPreferenceKeyDisclaimerAccepted", false)

    // The app preference which indicates whether the reminder for appointment is on or not.
    private val REMINDER_STATE_PREF = Pair("SettingsPreferenceKeyAppointmentReminder", false)

    // The app preference for the value of the appointment reminder time in minutes.
    private val REMINDER_TIME_PREF = Pair("SettingsPreferenceKeyAppointmentReminderTime", 0)

    // The app preference for the locale setting.
    private val LOCALE_PREF = Pair("SettingsPreferenceKeySelectedLanguage", "en")

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    private fun settingsVersion1(appUpdate: Boolean) {
        Log.i(TAG, "v1")

        disclaimerAccepted = false
        reminderOn = false
        reminderTime = 0
    }

    /**
     * Updates the internal setting with default values for not yet initialized properties.
     * This method has to be called after an app start so when the user starts the app the first time all setting properties get initialized properly.
     *
     * @return `true` if an update has been performed, `false` when the settings were already up to date.
     */
    fun updateSettings(): Boolean {
        val currentSettingVersion = settingsVersion
        if (currentSettingVersion >= Const.InternalSetting.latestVersionNumber) {
            // Settings are up to date.
            return false
        }

        // Settings version needs to be updated.
        // `appUpdate` indicates that this isn't a fresh app install, but an app update
        // so some properties may have already been set.
        val appUpdate = currentSettingVersion > 0
        Log.i(TAG, (if (appUpdate) "Updating" else "Initializing") + "internal settings")

        // Start updating for each version in case the user has skipped some updates.
        if (currentSettingVersion < Const.InternalSetting.settingsVersion1) {
            settingsVersion1(appUpdate)
        }

        // Add new steps here...

        // Finish update.
        settingsVersion = Const.InternalSetting.latestVersionNumber
        Log.i(TAG, "Internal settings updated")

        return true
    }

    // The current setting's version.
    // Returns 0 when none has been set.
    var settingsVersion: Int
        get() = preferences.getInt(SETTINGS_VERSION_PREF.first, SETTINGS_VERSION_PREF.second)
        set(value) = preferences.edit {
            it.putInt(SETTINGS_VERSION_PREF.first, value)
        }

    // Whether the user has accepted the disclaimer or not.
    // `true` if accepted, otherwise `false`.
    var disclaimerAccepted: Boolean
        get() = preferences.getBoolean(DISCLAIMER_ACCEPT_PREF.first, DISCLAIMER_ACCEPT_PREF.second)
        set(value) = preferences.edit {
            it.putBoolean(DISCLAIMER_ACCEPT_PREF.first, value)
        }

    // Whether the user wants to get reminded of his appointments,
    // so local notifications are enabled or not.
    var reminderOn: Boolean
        get() = preferences.getBoolean(REMINDER_STATE_PREF.first, REMINDER_STATE_PREF.second)
        set(value) = preferences.edit {
            it.putBoolean(REMINDER_STATE_PREF.first, value)
        }

    // The time in minutes of a reminder for the appointments.
    var reminderTime: Int
        get() = preferences.getInt(REMINDER_TIME_PREF.first, REMINDER_TIME_PREF.second)
        set(value) = preferences.edit {
            it.putInt(REMINDER_TIME_PREF.first, value)
        }

    // The locale string that is currently used in the app.
    var selectedLanguage: String?
        get() = preferences.getString(LOCALE_PREF.first, LOCALE_PREF.second)
        set(value) = preferences.edit {
            it.putString(LOCALE_PREF.first, value)
        }
}