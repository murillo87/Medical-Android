package com.appamedix.makula.scenes.reminder.table.checkboxcell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

/* The view model for the `ReminderCheckboxCell` */
class ReminderCheckboxCellViewModel(model: ReminderCheckboxCellModel)
    : BaseCellViewModel(ViewCellType.ReminderCheckboxCell) {
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The cell's checked state.
    var checked: Boolean = false
    // The listener to inform about cell actions.
    var listener: ReminderCheckboxCellListener? = null

    init {
        this.largeStyle = model.largeStyle
        this.checked = model.checked
        this.listener = model.listener
    }
}