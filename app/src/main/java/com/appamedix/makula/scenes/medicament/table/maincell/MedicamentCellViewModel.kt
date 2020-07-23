package com.appamedix.makula.scenes.medicament.table.maincell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

class MedicamentCellViewModel(model: MedicamentCellModel) : BaseCellViewModel(ViewCellType.MedicamentCell) {
    // The cell's title.
    var title: String = ""
    // The cell's editable state.
    var editable: Boolean = false
    // The cell's selected state.
    var selected: Boolean = false
    // When true the title will use an extra large font size (e.g. landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The listener to inform about cell actions.
    var listener: MedicamentCellListener? = null

    init {
        this.title = model.title
        this.editable = model.editable
        this.selected = model.selected
        this.largeStyle = model.largeStyle
        this.listener = model.listener
    }
}