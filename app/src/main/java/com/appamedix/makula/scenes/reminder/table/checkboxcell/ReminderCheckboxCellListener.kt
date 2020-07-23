package com.appamedix.makula.scenes.reminder.table.checkboxcell

interface ReminderCheckboxCellListener {
    /**
     * Informs that the cell has been pressed.
     *
     * @param position: The cell index to be pressed.
     */
    fun onCellItemClicked(position: Int)
}