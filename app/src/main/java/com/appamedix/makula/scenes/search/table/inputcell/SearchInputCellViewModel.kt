package com.appamedix.makula.scenes.search.table.inputcell

import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ViewCellType

/* The view model for the `SearchInputCell` */
class SearchInputCellViewModel(model: SearchInputCellModel)
    : BaseCellViewModel(ViewCellType.SearchInputCell) {
    // When true the title will use an extra large font size (e.g. for landscape mode),
    // while false uses the default size (e.g. for portrait).
    var largeStyle: Boolean = false
    // The textfield's content text if any should be shown. Leave to `null` to show the placeholder.
    var searchText: String? = null
    // The listener to inform about cell actions.
    var listener: SearchInputCellListener? = null

    init {
        this.largeStyle = model.largeStyle
        this.searchText = model.searchText
        this.listener = model.listener
    }
}