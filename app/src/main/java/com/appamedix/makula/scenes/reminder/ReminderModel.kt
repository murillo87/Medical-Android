package com.appamedix.makula.scenes.reminder

data class ReminderDisplayModel(
        // The activity for this scene.
        val activity: ReminderSceneActivity,
        // Whether the display should use a large style, e.g. for landscape
        // or not in which case the default style is used, e.g. portrait.
        val largeStyle: Boolean,
        // Whether the checkbox for the reminder state is checked or not.
        val checkboxChecked: Boolean,
        // The picker value as time in minutes.
        val pickerValue: Int
)