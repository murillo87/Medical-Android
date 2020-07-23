package com.appamedix.makula.scenes.diagnosis.table.maincell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.diagnosis.table.DiagnosisType
import com.appamedix.makula.types.ViewCellType

class DiagnosisCellViewModel(model: DiagnosisCellModel) : BaseCellViewModel(ViewCellType.DiagnosisCell) {
    // The diagnosis type of this cell.
    var diagnosisType: DiagnosisType = DiagnosisType.AMD
    // The selected state.
    var selected: Boolean = false
    // The cell's title.
    var title: String = ""
    // The cell's subtitle.
    var subtitle: String = ""
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The listener to inform about cell actions.
    var listener: DiagnosisCellListener? = null

    init {
        this.diagnosisType = model.diagnosisType
        this.selected = model.selected
        this.title = model.title
        this.subtitle = model.subtitle
        this.largeStyle = model.largeStyle
        this.listener = model.listener
    }
}