package com.appamedix.makula.utils

import android.content.Context
import android.os.Build
import android.widget.DatePicker
import android.widget.TimePicker
import com.appamedix.makula.R
import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    companion object {

        /**
         * Returns a date with given date value.
         *
         * @param year: The year value
         * @param month: The month value
         * @param day: The day value
         * @param hour: The hour value
         * @param minute: The minute value
         * @param second: The second value
         * @return The date object
         */
        fun setDateWithDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, second)

            return calendar.time
        }

        /**
         * Returns the year value from the given date.
         *
         * @param: The date.
         * @return The year value.
         */
        fun getYear(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date

            return calendar.get(Calendar.YEAR)
        }

        /**
         * Returns the month value from the given date.
         *
         * @param: The date.
         * @return The month value.
         */
        fun getMonth(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date

            return calendar.get(Calendar.MONTH)
        }

        /**
         * Returns the day value from the given date.
         *
         * @param: The date.
         * @return The day value.
         */
        fun getDay(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date

            return calendar.get(Calendar.DAY_OF_MONTH)
        }

        /**
         * Returns the start date of this week.
         *
         * @param date: The date to calculate
         * @return The start date.
         */
        fun startOfWeek(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.firstDayOfWeek = Calendar.MONDAY

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.clear(Calendar.MINUTE)
            calendar.clear(Calendar.SECOND)
            calendar.clear(Calendar.MILLISECOND)

            calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
            return calendar.time
        }

        /**
         * Returns the start date of this month.
         *
         * @param date: The date to calculate
         * @return The start date.
         */
        fun startOfMonth(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.clear(Calendar.MINUTE)
            calendar.clear(Calendar.SECOND)
            calendar.clear(Calendar.MILLISECOND)

            calendar.set(Calendar.DAY_OF_MONTH, 1)
            return calendar.time
        }

        /**
         * Returns the end date of this month.
         *
         * @param date: The date to calculate
         * @return The end date.
         */
        fun endOfMonth(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date

            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.clear(Calendar.MINUTE)
            calendar.clear(Calendar.SECOND)
            calendar.clear(Calendar.MILLISECOND)

            val lastDate = calendar.getActualMaximum(Calendar.DATE)
            calendar.set(Calendar.DATE, lastDate)
            return calendar.time
        }

        /**
         * Returns a date with time zeroed.
         * @param date: The date to be trancated
         * @return The truncated date
         */
        fun truncateTime(date: Date): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            return calendar.time
        }

        /**
         * Returns a date added as the given amount of days.
         * @param date: The date to be increased
         * @param numberOfDays: The increased amount of days
         * @return The increased date
         */
        fun increaseDay(date: Date, numberOfDays: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.DATE, numberOfDays)

            return calendar.time
        }

        /**
         * Returns a date added as the given amount of months.
         * @param date: The date to be increased
         * @param numberOfMonths: The increased amount of months
         * @return The increased date
         */
        fun increaseMonth(date: Date, numberOfMonths: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MONTH, numberOfMonths)

            return calendar.time
        }

        /**
         * Returns a date added as the given amount of minutes.
         * @param date: The date to be increased
         * @param numberOfMinutes: The increased amount of minutes
         * @return The increased date
         */
        fun increaseMinutes(date: Date, numberOfMinutes: Int): Date {
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.add(Calendar.MINUTE, numberOfMinutes)

            return calendar.time
        }

        /**
         * Checks whether two this and another dates fall in the same month.
         *
         * @param thisDate: A date to compare with.
         * @param otherDate: Another date to compare with.
         * @return `true` if both dates share the same month and year, otherwise `false`.
         */
        fun isInSameMonth(thisDate: Date, otherDate: Date): Boolean {
            val thisCalendar = Calendar.getInstance()
            thisCalendar.time = thisDate
            val otherCalendar = Calendar.getInstance()
            otherCalendar.time = otherDate

            return (thisCalendar.get(Calendar.YEAR) == otherCalendar.get(Calendar.YEAR) &&
                    thisCalendar.get(Calendar.MONTH) == otherCalendar.get(Calendar.MONTH))
        }

        /**
         * Returns the max days in month for the given date.
         *
         * @param date: The date to check.
         * @return The max number of days.
         */
        fun getMaxDaysInMonth(date: Date): Int {
            val calendar = Calendar.getInstance()
            calendar.time = date

            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        }

        /**
         * Returns the number of months between two dates.
         *
         * @param startDate: A date to calculate.
         * @param endDate: Another date to calculate.
         * @return The number of months.
         */
        fun monthsBetweenDates(startDate: Date, endDate: Date): Int {
            val startCalendar = Calendar.getInstance()
            startCalendar.time = startOfMonth(startDate)

            val endCalendar = Calendar.getInstance()
            endCalendar.time = startOfMonth(endDate)

            var monthsBetween = 0
            var dateDiff = endCalendar.get(Calendar.DAY_OF_MONTH) - startCalendar.get(Calendar.DAY_OF_MONTH)

            if (dateDiff < 0) {
                val borrow = endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
                dateDiff = (endCalendar.get(Calendar.DAY_OF_MONTH) + borrow) - startCalendar.get(Calendar.DAY_OF_MONTH)
                monthsBetween -= 1

                if (dateDiff > 0) monthsBetween += 1
            } else {
                monthsBetween += 1
            }

            monthsBetween += endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
            monthsBetween += (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12

            return monthsBetween
        }

        /**
         * Returns the difference of months between two dates.
         *
         * @param startDate: A date to calculate.
         * @param endDate: Another date to calculate.
         * @return The month difference.
         */
        fun monthDiffBetweenDates(startDate: Date, endDate: Date): Int {
            val startCalendar = Calendar.getInstance()
            startCalendar.time = startDate

            val endCalendar = Calendar.getInstance()
            endCalendar.time = endDate

            var monthsBetween = 0
            monthsBetween += endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH)
            monthsBetween += (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12

            return monthsBetween
        }

        /**
         * Returns a date set by date and time pick controllers.
         *
         * @param datePicker: The date picker controller.
         * @param timePicker: The time picker controller.
         * @return The current set date
         */
        fun getDateFromPicker(datePicker: DatePicker, timePicker: TimePicker): Date {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year

            val hour = if (Build.VERSION.SDK_INT >= 23) timePicker.hour else timePicker.currentHour
            val minute = if (Build.VERSION.SDK_INT >= 23) timePicker.minute else timePicker.currentMinute

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, hour, minute)

            return calendar.time
        }

        /**
         * Returns a formatted string for the give date with the given patter.
         *
         * @param date: The date to format.
         * @param pattern: The pattern string, e.g. "EEE, dd.MM.yyyy HH:mm:ss a, z"
         * @return The formatted date as string.
         */
        fun toSimpleString(date: Date, pattern: String): String {
            val format = SimpleDateFormat(pattern, Locale.getDefault())
            return format.format(date)
        }

        /**
         * Prints the date with the weekday prefix as `Mo 01.02.18`.
         *
         * @param context: The context called from.
         * @param date: The date to format.
         * @return The formatted date as string.
         */
        fun toStringWithWeekday(context: Context, date: Date): String {
            // Gets formatted date.
            var pattern = context.getString(R.string.commonDateFormat)
            val formattedDate = toSimpleString(date, pattern)

            // Gets week day string.
            val calendar = Calendar.getInstance()
            calendar.time = date
            calendar.firstDayOfWeek = Calendar.MONDAY
            val weekday = calendar.get(Calendar.DAY_OF_WEEK)

            val weekdayString = when (weekday) {
                Calendar.SUNDAY -> context.getString(R.string.commonDateWeekdaySu)
                Calendar.MONDAY -> context.getString(R.string.commonDateWeekdayMo)
                Calendar.TUESDAY -> context.getString(R.string.commonDateWeekdayTu)
                Calendar.WEDNESDAY -> context.getString(R.string.commonDateWeekdayWe)
                Calendar.THURSDAY -> context.getString(R.string.commonDateWeekdayTh)
                Calendar.FRIDAY -> context.getString(R.string.commonDateWeekdayFr)
                Calendar.SATURDAY -> context.getString(R.string.commonDateWeekdaySa)
                else -> throw IllegalArgumentException("Invalid week day")
            }

            pattern = context.getString(R.string.commonDateWithWeekdayFormat)

            return String.format(pattern, weekdayString, formattedDate)
        }
    }
}