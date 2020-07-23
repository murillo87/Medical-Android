package com.appamedix.makula.scenes.contactdetail.table.maincell

import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.contactdetail.ContactInfoType
import com.appamedix.makula.types.ViewCellType

/* The view model for the `ContactDetailCell` */
class ContactDetailCellViewModel(model: ContactDetailCellModel)
    : BaseCellViewModel(ViewCellType.ContactDetailCell) {
    // The type of the cell content.
    var type: ContactInfoType = ContactInfoType.Name
    // The cell's title to show if the title label should be shown rather than the text field.
    var title: String? = null
    // The cell's color in normal state.
    var defaultColor: Int = R.color.lightMain
    // The cell's color in selected state.
    var highlightColor: Int = R.color.white
    // Whether the cell's content can be deleted or not.
    var editable: Boolean = true
    // Whether the cell has a custom action or not.
    var actable: Boolean = false
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false

    init {
        this.type = model.type
        this.title = model.title
        this.defaultColor = model.defaultColor
        this.highlightColor = model.highlightColor
        this.editable = model.editable
        this.actable = model.actable
        this.largeStyle = model.largeStyle
    }
}