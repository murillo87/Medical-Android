package com.appamedix.makula.views.navigation

/* The interface to inform about navigation item actions */
interface NavigationViewListener {
    /**
     * Informs that the left button has been clicked.
     */
    fun leftButtonClicked()

    /**
     * Informs that the right button has been clicked.
     */
    fun rightButtonClicked()
}