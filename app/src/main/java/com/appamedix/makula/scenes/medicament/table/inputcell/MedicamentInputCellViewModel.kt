package com.appamedix.makula.scenes.medicament.table.inputcell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

class MedicamentInputCellViewModel(model: MedicamentInputCellModel)
    : BaseCellViewModel(ViewCellType.MedicamentInputCell) {
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The listener to inform about cell actions.
    var listener: MedicamentInputCellListener? = null

    init {
        this.largeStyle = model.largeStyle
        this.listener = model.listener
    }
}