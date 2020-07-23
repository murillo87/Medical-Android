package com.appamedix.makula.scenes.reminder.table.pickercell

interface ReminderPickerCellListener {
    /**
     * Informs that the picker's value has changed.
     *
     * @param newValue: The changed value.
     */
    fun pickerValueChanged(newValue: Int)
}