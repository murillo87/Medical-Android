package com.appamedix.makula.scenes.reminder.table.pickercell

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.NumberPicker
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

/* The custom class for the reminder picker cell view */
class ReminderPickerCell(private val context: Context, view: View)
    : RecyclerView.ViewHolder(view) {

    // The UI elements to consist this cell view.
    private val pickerHour: NumberPicker = view.findViewById(R.id.number_picker_hour)
    private val pickerMinute: NumberPicker = view.findViewById(R.id.number_picker_minute)

    // The currently selected value for the picker type shown in the picker (or at least close to it).
    private var selectedValue: Int
        get() = pickerHour.value * 60 + pickerMinute.value * 5
        set(value) {
            setPickerValue(value)
        }

    private var listener: ReminderPickerCellListener? = null

    fun bindItems(model: ReminderPickerCellViewModel) {
        this.listener = model.listener

        // Setup views
        pickerHour.minValue = 0
        pickerHour.maxValue = Const.Reminder.pickerMaxHours
        pickerHour.wrapSelectorWheel = false

        val displayHours: ArrayList<String> = arrayListOf()
        for (i in pickerHour.minValue..pickerHour.maxValue) {
            val formatString = context.getString(R.string.reminderPickerHour)
            val hourString = String.format(formatString, i)
            displayHours.add(hourString)
        }
        pickerHour.displayedValues = displayHours.toTypedArray()

        pickerMinute.minValue = 0
        pickerMinute.maxValue = 60 / Const.Reminder.pickerMinuteInterval - 1
        pickerMinute.wrapSelectorWheel = false

        val displayMinutes: ArrayList<String> = arrayListOf()
        for (i in pickerMinute.minValue..pickerMinute.maxValue) {
            val formatString = context.getString(R.string.reminderPickerMinute)
            val hourString = String.format(formatString, i * 5)
            displayMinutes.add(hourString)
        }
        pickerMinute.displayedValues = displayMinutes.toTypedArray()

        selectedValue = model.value

        // Register for callback listeners.
        pickerHour.setOnValueChangedListener { _, _, _ ->
            listener?.pickerValueChanged(selectedValue)
        }
        pickerMinute.setOnValueChangedListener { _, _, _ ->
            listener?.pickerValueChanged(selectedValue)
        }
    }

    /**
     * Sets a value to show it in the pickers.
     *
     * @param newValue: The value to set.
     */
    private fun setPickerValue(newValue: Int) {
        pickerHour.value = newValue / 60
        pickerMinute.value = (newValue % 60) / Const.Reminder.pickerMinuteInterval
    }
}