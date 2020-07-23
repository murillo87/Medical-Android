package com.appamedix.makula.scenes.contactdetail.table.maincell

import com.appamedix.makula.scenes.contactdetail.ContactInfoType

/* The model for the `ContactDetailCell` */
data class ContactDetailCellModel(
        // The type of the cell content.
        val type: ContactInfoType,
        // The cell's title to show if the title label should be shown rather than the text field.
        val title: String?,
        // The cell's color in normal state.
        val defaultColor: Int,
        // The cell's color in selected state.
        val highlightColor: Int,
        // Whether the cell's content can be deleted or not.
        val editable: Boolean,
        // Whether the cell has a custom action or not.
        val actable: Boolean,
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean
)