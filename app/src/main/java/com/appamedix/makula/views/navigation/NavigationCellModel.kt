package com.appamedix.makula.views.navigation

import com.appamedix.makula.types.ImageButtonType

/* The model for the `NavigationViewCell` */
class NavigationCellModel(
        // The navigation's title.
        val title: String,
        // The color of the navigation's title and its separator.
        val color: Int,
        // When `true` an extra large font size (e.g. for landscape mode) and scaled icons will be used,
        // while `false` the default size (e.g. for portrait) uses.
        val largeStyle: Boolean,
        // The visibility of the separator line. Set to `false` to hide it, `true` to show it.
        val separatorVisible: Boolean,
        // The visibility of the left button. Set to `false` to hide it, `true` to show it.
        val leftButtonVisible: Boolean,
        // The type of button the left button shows.
        val leftButtonType: ImageButtonType?,
        // The visibility of the right button. Set to `false` to hide it, `true` to show it.
        val rightButtonVisible: Boolean,
        // The type of button the right button shows.
        val rightButtonType: ImageButtonType?,
        // The interface to inform about actions.
        val listener: NavigationViewListener?
)