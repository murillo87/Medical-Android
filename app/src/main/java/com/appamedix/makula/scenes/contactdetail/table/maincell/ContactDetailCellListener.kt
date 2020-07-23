package com.appamedix.makula.scenes.contactdetail.table.maincell

interface ContactDetailCellListener {
    /**
     * Informs that the delete button has been clicked from the swipe menu.
     * This is called in the case that both delete buttons in the left and right swipe menu.
     *
     * @param model: The contact cell view model for the deleted cell.
     */
    fun deleteButtonClicked(model: ContactDetailCellViewModel)

    /**
     * Informs that the return key ('\n') has been pressed in the edit text.
     *
     * @param model: The contact cell view model for the focused cell.
     */
    fun editTextDidEndEditing(model: ContactDetailCellViewModel)

    /**
     * Informs that the cell has been pressed for the custom action.
     *
     * @param model: The contact cell view model for the clicked cell.
     */
    fun onCellItemClicked(model: ContactDetailCellViewModel)
}