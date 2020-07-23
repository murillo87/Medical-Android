package com.appamedix.makula.constants

import android.graphics.Rect

class Const {
    /* The available build configurations of this project */
    object BuildConfig {
        // Development build.
        const val devProduction = "devProduction"

        // Final release build.
        const val finalProduction = "finalProduction"
    }

    /* Any global size values, e.g. widths & heights */
    object Size {
        // The factor to apply on views when in landscape where they should be shown scaled up.
        const val landscapeScaleFactor = 1.5f

        // A default size for adding between two view elements so they won't be too close to each other.
        // Use this value for creating constraints between views when there should be a gap between them.
        const val defaultGap = 6.0f

        // The gap for a title label to the center.
        const val middleGap = 10.0f

        // The estimation height for default cells.
        const val cellEstimatedDefaultHeight = 73.0f

        // The min height for a default title label in a cell.
        const val cellDefaultLabelMinHeight = 52.0f

        // The default thickness of the separator lines in normal mode.
        const val separatorThicknessNormal = 2.5f

        // The thickness of the separator lines in large mode.
        const val separatorThicknessLarge = 4.0f

        // The default height of navigation buttons.
        const val navButtonHeightNormal = 64.0f

        // The default padding for top and bottom of navigation buttons.
        const val navButtonPaddingNormal = 20.0f

        // The default height of split radio buttons.
        const val radioButtonHeightNormal = 64.0f

        // The default padding for top and bottom of split radio buttons.
        const val radioButtonPaddingNormal = 10.0f

        // The default height of the drag button.
        const val dragButtonHeightNormal = 36.0f

        // The default height of the arrow icon.
        const val arrowIconHeightNormal = 25.0f

        // The default width of the swipe menu.
        const val swipeMenuWidthNormal = 120.0f

        // The default thickness of the graph lines in normal mode.
        const val graphLineThicknessNormal = 5.0f

        // The thickness of the graph lines in large mode.
        const val graphLineThicknessLarge = 8.0f
    }

    /* Any font sizes in landscape mode */
    object Font {
        // The default font size of the main title, e.g. the header in portrait mode.
        const val titleDefault = 32.0f

        // The large font size of the main title, e.g. the header in landscape mode.
        const val titleLarge = 57.0f

        // The default font size of the headline, e.g. the menu cells in portrait mode.
        const val headlineDefault = 32.0f

        // The large font size of the headline, e.g. the menu cells in landscape mode.
        const val headlineLarge = 57.0f

        // The default font size of the content text #1, e.g. the disclaimer in portrait mode.
        const val content1Default = 26.0f

        // The large font size of the content text #1, e.g. the disclaimer in landscape mode.
        const val content1Large = 46.0f

        // The default font size of the content text #2, e.g. the week names in the calendar in portrait mode.
        const val content2Default = 24.0f

        // The large font size of the content text #2, e.g. the week names in the calendar in landscape mode.
        const val content2Large = 43.0f

        // The default font size for the numbers in the calendar in portrait mode.
        const val numbersDefault = 30.0f

        // The large font size for the numbers in the calendar in landscape mode.
        const val numbersLarge = 54.0f

        // The big font size for the reading test.
        const val readingtestBig = 42.0f

        // The large font size for the reading test.
        const val readingtestLarge = 32.0f

        // The medium font size for the reading test.
        const val readingtestMedium = 24.0f

        // The small font size for the reading test.
        const val readingtestSmall = 18.0f

        // The little font size for the reading test.
        const val readingtestLittle = 15.0f

        // The tiny font size for the reading test.
        const val readingtestTiny = 12.0f

        // The default font size for the numbers in the graph.
        const val graphNumbersDefault = 22.0f

        // The large font size for the numbers in the graph.
        const val graphNumbersLarge = 30.0f

        // The default font size for the text in the graph.
        const val graphTextDefault = 22.0f

        // The large font size for the text in the graph.
        const val graphTextLarge = 30.0f
    }

    /* Any time interval constants */
    object Time {
        // The duration for default animations.
        const val defaultAnimationDuration: Long = 250

        // The delay before the splash automatically transits.
        const val transitionDelay: Long = 2000
    }

    /* Data model value borders */
    object Data {
        // The lower boundary of the visus value for an eye.
        const val visusMinValue = 0

        // The upper boundary of the visus value for an eye.
        const val visusMaxValue = 12

        // The lower boundary of the NHD value.
        const val nhdMinValue = 220

        // The upper boundary of the NHD value.
        const val nhdMaxValue = 460
    }

    /* Constants for the appointment date picker */
    object AppointmentDatePicker {
        // The interval for the time picker in minutes.
        const val pickerMinuteInterval = 5
    }

    /* Constants for the calendar scene */
    object Calendar {
        // The number of months to display in the calendar before the current month.
        const val monthsBefore = 24

        // The number of months to display in the calendar after the current month.
        const val monthsAfter = 24

        // The spacing size for the left and right margin.
        val margin = Rect(22, 32, 22, 36)

        // The spacing size between grid cells in week cell view.
        const val spacingBetweenGrid = 8
    }

    /* Constants for the graph scene */
    object Graph {
        // The number of steps to show on the Y axis.
        const val yAxisSteps: Float = 12.0f

        // The number of steps to show on the X axis which represents how many months should be shown.
        const val xAxisSteps: Float = 24.0f

        // The number of months to show at once horizontally.
        const val xAxisStepsPerPage: Float = 3.0f

        // The factor to use for scaling the dots compared to the normal line width.
        const val dotFactor: Float = 2.0f

        // The stroke width for the graph lines.
        const val lineWidth: Int = 6
    }

    /* Constants for the reminder scene */
    object Reminder {
        // The interval of minutes to show in the picker, e.g. every 5 minutes.
        const val pickerMinuteInterval: Int = 5

        // The maximum of hours to display in the picker.
        const val pickerMaxHours: Int = 23
    }

    /* Constants for the internal settings */
    object InternalSetting {
        // The internal settings version number for app v1.0.
        const val settingsVersion1 = 1

        // The latest version number to which the internal settings should be updated.
        // Assign new value when a new settings version is introduced.
        const val latestVersionNumber = settingsVersion1
    }

    /* Constants for the data model manager */
    object DataModelManager {
        // The file name of the database.
        // The absolute path to the database will be
        // `/data/data/{packageName}/files/{databasePathName}/{databaseFileName}`.
        const val databaseFileName = "makula.realm"

        // The path where the database is written into (inclusive slash at the end).
        const val databasePathName = "database/"

        // The zip file name.
        // The absolute path to the zip file will be
        // `/data/data/{packageName}/files/{backupPathName}/{backupFileName}`.
        const val backupFileName = "archive.zip"

        // The path where the zip file is written into (inclusive slash at the end).
        const val backupPathName = "backup/"

        // The data model version number for a blank no schema database to initiate the initial migration.
        // 0 doesn't work so we start with 1
        const val modelVersion0: Long = 1

        // The data model version number for app v1.0.
        const val modelVersion1: Long = 2

        // The latest version number to which the database should be updated.
        // Assign new value when a new model version is introduced.
        const val latestVersionNumber: Long = modelVersion1
    }
}