package com.appamedix.makula.scenes.datepicker

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.calendar.CalendarModel
import com.appamedix.makula.scenes.calendar.CalendarSceneActivity
import com.appamedix.makula.types.AppointmentType
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.datamodel.appointment.createAppointmentObject
import com.appamedix.makula.worker.datamodel.appointment.getAppointmentObjects
import com.appamedix.makula.worker.datamodel.appointment.setAppointmentModel
import com.appamedix.makula.worker.notification.NotificationScheduler
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_appointment_date_picker_scene.*
import java.util.*

class AppointmentDatePickerSceneActivity : BaseActivity(), NavigationViewListener {

    companion object {
        const val DATA_MODEL = "data_model"
    }

    private var isLandscape: Boolean = false
    private var appointmentType: AppointmentType? = null

    private lateinit var datePicker: DatePicker
    private lateinit var timePicker: TimePicker
    private lateinit var saveButton: Button

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_date_picker_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        intent.extras?.let {
            val dataModel = it.getParcelable<AppointmentDatePickerModel>(DATA_MODEL)
            appointmentType = dataModel?.appointmentType
        }

        // Get instances of UI elements.
        datePicker = findViewById(R.id.date_picker)
        timePicker = findViewById(R.id.time_picker)
        saveButton = findViewById(R.id.save_button)

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(R.string.datePickerTitle),
                R.color.white,
                isLandscape,
                false,
                true,
                ImageButtonType.Back,
                false,
                ImageButtonType.Speaker,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        // Set up time picker.
        timePicker.setIs24HourView(true)

        // Set click listener to save button.
        saveButton.setOnClickListener {
            val date = DateUtils.getDateFromPicker(datePicker, timePicker)
            saveButtonPressed(date)
        }

        // Apply large style to UI elements in landscape mode.
        if (isLandscape) {
            saveButton.textSize = Const.Font.content1Large
        }

        // Updates the values of date and time pickers if there is an appointment.
        updatePickers()
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    private fun updatePickers() {
        // Get the date from an existing appointment or use the today's.
        var appointmentDate = Date()
        val type = appointmentType ?: AppointmentType.Other
        val appointmentsForDate = realm.getAppointmentObjects(appointmentDate, type)
        if (appointmentsForDate.size > 0) {
            appointmentsForDate.first()?.let {
                appointmentDate = it.getAppointmentDate()
            }
        }

        // Inform the display to show the correct date for the picker.
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = appointmentDate
        val day = calendar.get(Calendar.DATE)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)
        datePicker.updateDate(year, month, day)

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        if (Build.VERSION.SDK_INT >= 23) {
            timePicker.hour = hour
            timePicker.minute = minute
        } else {
            timePicker.currentHour = hour
            timePicker.currentMinute = minute
        }
    }

    private fun saveButtonPressed(withDate: Date) {
        val type = appointmentType ?: return

        // Get any existing appointments for the date.
        val appointmentsForDate = realm.getAppointmentObjects(withDate, type)

        // Persist appointment.
        val existingAppointment = if (appointmentsForDate.size > 0) appointmentsForDate.first() else null
        if (existingAppointment == null) {
            // Create new entry when none has been updated.
            realm.createAppointmentObject(type, withDate)
        } else {
            // Found same type on the day, update it.
            if (!realm.setAppointmentModel(existingAppointment, withDate)) {
                databaseWriteError()
                return
            }
        }

        // Setup local notification.
        NotificationScheduler.setReminder(applicationContext, null)

        // Route to calendar scene.
        val calendarModel = CalendarModel(withDate, true)
        val intent = Intent(this, CalendarSceneActivity::class.java)
        intent.putExtra(CalendarSceneActivity.CALENDAR_MODEL, calendarModel)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    /**
     * Shows database error
     */
    private fun databaseWriteError() {
        Toast.makeText(this, R.string.databaseWriteErrorMessage, Toast.LENGTH_SHORT).show()
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
    }
}
