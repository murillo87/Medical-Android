package com.appamedix.makula.scenes.contact.table.maincell

import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

/* The view model for the `ContactCell` */
class ContactCellViewModel(model: ContactCellModel) : BaseCellViewModel(ViewCellType.ContactCell) {
    // The cell's title.
    var title: String = ""
    // The cell's default color.
    var defaultColor: Int = R.color.lightMain
    // The cell's highlight color.
    var highlightColor: Int = R.color.white
    // The cell's editable state. Defaults to `false`, `true` to be editable.
    var editable: Boolean = false
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The listener for the swipe menu actions.
    var listener: ContactCellListener? = null

    init {
        this.title = model.title
        this.defaultColor = model.defaultColor
        this.highlightColor = model.highlightColor
        this.editable = model.editable
        this.largeStyle = model.largeStyle
        this.listener = model.listener
    }
}