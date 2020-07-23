package com.appamedix.makula.scenes.contact.table.maincell

/* The model for the `ContactCell` */
data class ContactCellModel(
        // The cell's title.
        val title: String,
        // The cell's default color.
        val defaultColor: Int,
        // The cell's highlight color.
        val highlightColor: Int,
        // The cell's editable state. Defaults to `false`, `true` to be editable.
        val editable: Boolean,
        // When true the title will use an extra large font size (e.g. for landscape mode),
        // while false uses the default size (e.g. for portrait).
        val largeStyle: Boolean,
        // The listener for the swipe menu actions.
        val listener: ContactCellListener?
)