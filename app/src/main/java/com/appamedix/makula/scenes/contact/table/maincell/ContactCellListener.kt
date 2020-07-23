package com.appamedix.makula.scenes.contact.table.maincell

/* The interface to inform about swipe item actions */
interface ContactCellListener {
    /**
     * Informs that the delete button has been clicked from the swipe menu.
     * This is called in the case that both delete buttons in the left and right swipe menu.
     *
     * @param position: The index of the cell.
     */
    fun deleteButtonClicked(position: Int)
}