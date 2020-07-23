package com.appamedix.makula.views.navigation

import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.ViewCellType

/* The view model for the `NavigationViewCell` */
class NavigationCellViewModel(model: NavigationCellModel) : BaseCellViewModel(ViewCellType.Navigation) {
    // The navigation's title.
    var title: String = ""
    // The color of the navigation's title and its separator.
    var color: Int = R.color.white
    // When `true` an extra large font size (e.g. for landscape mode) and scaled icons will be used,
    // while `false` the default size (e.g. for portrait) uses.
    var largeStyle: Boolean = false
    // The visibility of the separator line. Set to `false` to hide it, `true` to show it.
    var separatorVisible: Boolean = true
    // The visibility of the left button. Set to `false` to hide it, `true` to show it.
    var leftButtonVisible: Boolean = true
    // The type of button the left button shows.
    var leftButtonType: ImageButtonType? = null
    // The visibility of the right button. Set to `false` to hide it, `true` to show it.
    var rightButtonVisible: Boolean = true
    // The type of button the right button shows.
    var rightButtonType: ImageButtonType? = null
    // The interface to inform about actions.
    var listener: NavigationViewListener? = null

    init {
        this.title = model.title
        this.color = model.color
        this.largeStyle = model.largeStyle
        this.separatorVisible = model.separatorVisible
        this.leftButtonVisible = model.leftButtonVisible
        this.leftButtonType = model.leftButtonType
        this.rightButtonVisible = model.rightButtonVisible
        this.rightButtonType = model.rightButtonType
        this.listener = model.listener
    }
}