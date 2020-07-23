package com.appamedix.makula.scenes.calendar

import com.appamedix.makula.R

/* The month type for this scene */
enum class CalendarMonth(val rawValue: Int) {
    January(1),
    February(2),
    March(3),
    April(4),
    May(5),
    June(6),
    July(7),
    August(8),
    September(9),
    October(10),
    November(11),
    December(12);

    /**
     * Returns the printable representation of the month.
     *
     * @return The resource id of the month's localized name.
     */
    fun titleString(): Int = when (this) {
        January -> R.string.monthJanuary
        February -> R.string.monthFebruary
        March -> R.string.monthMarch
        April -> R.string.monthApril
        May -> R.string.monthMay
        June -> R.string.monthJune
        July -> R.string.monthJuly
        August -> R.string.monthAugust
        September -> R.string.monthSeptember
        October -> R.string.monthOctober
        November -> R.string.monthNovember
        December -> R.string.monthDecember
    }

    companion object {
        /**
         * Returns CalendarMonth enum value for a given integer value.
         *
         * @param value: The int value correspond to enum.
         * @return The enum value
         */
        fun getType(value: Int): CalendarMonth = when (value) {
            1 -> January
            2 -> February
            3 -> March
            4 -> April
            5 -> May
            6 -> June
            7 -> July
            8 -> August
            9 -> September
            10 -> October
            11 -> November
            12 -> December
            else -> throw IllegalArgumentException("Invalid value")
        }
    }
}