package com.appamedix.makula.utils

import android.content.Context
import android.content.Context.WINDOW_SERVICE
import android.util.DisplayMetrics
import android.view.WindowManager

class DeviceUtils {
    companion object {
        /**
         * Returns device dimensions in pixel.
         *
         * @param: The context
         * @return The display metrics
         */
        fun getDeviceDimensions(context: Context): DisplayMetrics {
            val displayMetrics = DisplayMetrics()
            val windowManager = context.getSystemService(WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)

            return displayMetrics
        }
    }
}