package com.appamedix.makula.base

import android.arch.lifecycle.ViewModel
import com.appamedix.makula.types.ViewCellType

/**
 * A base class of view model for table view cells.
 * Derive all table cell view models from this class.
 */
open class BaseCellViewModel : ViewModel {
    // The type of table cell view model.
    var cellType: ViewCellType = ViewCellType.StaticTextCell

    constructor() : super()
    constructor(cellType: ViewCellType) : super() {
        this.cellType = cellType
    }
}