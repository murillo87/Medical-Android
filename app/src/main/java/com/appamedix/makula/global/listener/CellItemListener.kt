package com.appamedix.makula.global.listener

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

/* The interface to inform about cell actions */
interface CellItemListener {
    /**
     * Informs that a cell has been selected.
     *
     * @param model: The cell view model.
     * @param type: The cell type.
     */
    fun onItemClicked(model: BaseCellViewModel, type: ViewCellType)
}