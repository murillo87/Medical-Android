package com.appamedix.makula.utils

import android.app.Activity
import android.os.Handler

class Run {
    companion object {
        /**
         * Delays the current task for the given duration.
         *
         * @param delay: The duration to wait.
         */
        fun after(delay: Long, process: () -> Unit) {
            Handler().postDelayed({
                process()
            }, delay)
        }

        /**
         * Delays the current task for the given duration in UI main thread.
         *
         * @param delay: The duration to wait.
         * @param activity: The current running activity.
         */
        fun afterOnMain(delay: Long, activity: Activity, process: () -> Unit) {
            Handler().postDelayed({
                activity.runOnUiThread {
                    Runnable {
                        process()
                    }
                }
            }, delay)
        }
    }
}