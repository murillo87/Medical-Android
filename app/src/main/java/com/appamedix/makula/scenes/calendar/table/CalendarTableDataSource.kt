package com.appamedix.makula.scenes.calendar.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.calendar.CalendarMonth
import com.appamedix.makula.scenes.calendar.CalendarSceneActivity
import com.appamedix.makula.scenes.calendar.table.monthcell.CalendarMonthCellModel
import com.appamedix.makula.scenes.calendar.table.monthcell.CalendarMonthCellViewModel
import com.appamedix.makula.scenes.calendar.table.weekcell.CalendarWeekCellModel
import com.appamedix.makula.scenes.calendar.table.weekcell.CalendarWeekCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.worker.datamodel.amslertest.getAmslerTestObjects
import com.appamedix.makula.worker.datamodel.appointment.getAppointmentObjects
import com.appamedix.makula.worker.datamodel.histogram.getNhdObjects
import com.appamedix.makula.worker.datamodel.histogram.getVisusObjects
import com.appamedix.makula.worker.datamodel.note.getNoteObjects
import com.appamedix.makula.worker.datamodel.readingtest.getReadingTestObjects
import io.realm.Realm
import java.util.*
import kotlin.collections.ArrayList

class CalendarTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    var realm: Realm? = null

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param activity: The activity to use the data source.
     * @param largeStyle: The flag for the style. `true` in landscape mode.
     * @return A mutable live data list for the cells.
     */
    fun getCellData(activity: CalendarSceneActivity, largeStyle: Boolean): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Current month's date.
        val currentMonth = DateUtils.truncateTime(Date())
        var section: List<BaseCellViewModel>

        // Add the months before today.
        for (diff in -Const.Calendar.monthsBefore..-1) {
            val date = DateUtils.increaseMonth(currentMonth, diff)
            section = tableDataSection(date, activity, largeStyle)
            tableData += section
        }

        // Add the current month.
        section = tableDataSection(currentMonth, activity, largeStyle)
        tableData += section

        // Add the months after today.
        for (diff in 1..Const.Calendar.monthsAfter) {
            val date = DateUtils.increaseMonth(currentMonth, diff)
            section = tableDataSection(date, activity, largeStyle)
            tableData += section
        }

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a whole section containing the month's name and the week cells for a given month date.
     *
     * @param date: The month's date.
     * @param activity: The activity to use the data source.
     * @param largeStyle: The flag for the style. `true` in landscape mode.
     * @return The table section for the date.
     */
    private fun tableDataSection(date: Date, activity: CalendarSceneActivity, largeStyle: Boolean): List<BaseCellViewModel> {
        var section: List<BaseCellViewModel> = listOf()

        val calendar = Calendar.getInstance()
        calendar.time = date

        // Add the month cell for a given date.
        val monthValue = calendar.get(Calendar.MONTH)
        val yearValue = calendar.get(Calendar.YEAR)
        val monthType = CalendarMonth.getType(monthValue + 1)
        section += createMonthCellData(monthType, yearValue, activity, largeStyle)

        // Append the week cells.
        calendar.set(yearValue, monthValue, 1)
        val numOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstOfMonth = calendar.time
        calendar.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth - 1)
        val lastOfMonth = calendar.time
        var weekDate = firstOfMonth
        while (weekDate <= lastOfMonth) {
            section += createWeekCellData(weekDate, activity, largeStyle)
            calendar.time = weekDate
            calendar.add(Calendar.WEEK_OF_MONTH, 1)
            weekDate = calendar.time
        }
        // Append the last week, handled explicitly because of the calculation.
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val firstOfWeek = calendar.time
        if (firstOfWeek <= lastOfMonth) {
            section += createWeekCellData(lastOfMonth, activity, largeStyle)
        }

        return section
    }

    /**
     * Creates a month cell data for a given month and year value.
     *
     * @param month: The month value.
     * @param year: The year value.
     * @param activity: The activity to use the data source.
     * @param largeStyle: The flag for the style. `true` in landscape mode.
     * @return The list of month cell model.
     */
    private fun createMonthCellData(month: CalendarMonth,
                                    year: Int,
                                    activity: CalendarSceneActivity,
                                    largeStyle: Boolean): List<CalendarMonthCellViewModel> {
        val formatString = activity.getString(R.string.monthTitleFormat)
        val title = String.format(formatString, activity.getString(month.titleString()), year.toString())
        val monthCellModel = CalendarMonthCellModel(largeStyle, title)
        val monthCellViewModel = CalendarMonthCellViewModel(monthCellModel)

        return arrayListOf(monthCellViewModel)
    }

    /**
     * Creates the week cell for a given week date.
     *
     * @param weekDate: The month's date.
     * @param activity: The activity to use the data source.
     * @param largeStyle: The flag for the style. `true` in landscape mode.
     * @return The list of week cell model.
     */
    private fun createWeekCellData(weekDate: Date, activity: CalendarSceneActivity, largeStyle: Boolean): List<CalendarWeekCellViewModel> {
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.time = weekDate

        // Go through all days of the week.
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        val firstOfWeek = calendar.time
        val dayColors = mutableMapOf<Int, Int?>()
        val dayTexts = mutableMapOf<Int, String?>()
        for (dayIndex in 0..6) {
            // Make sure the day's date is shown in the month's week cell.
            val dayDate = DateUtils.increaseDay(firstOfWeek, dayIndex)
            calendar.time = dayDate
            val dayNumber = calendar.get(Calendar.DAY_OF_MONTH)
            val shown = DateUtils.isInSameMonth(weekDate, dayDate)

            // Save the day's number as text for the day label.
            val formatString = activity.getString(R.string.dayNumberFormat)
            val dayString = if (shown) String.format(formatString, dayNumber) else null
            dayTexts[dayIndex] = dayString

            // Determine the color for the day.
            dayColors[dayIndex] = null
            val realm = realm ?: return emptyList()
            val appointmentResults = realm.getAppointmentObjects(dayDate)
            when {
                appointmentResults.size == 1 -> {
                    // Single appointment, use the appointment's color.
                    val appointment = appointmentResults.first()
                    dayColors[dayIndex] = appointment?.getAppointmentType()?.defaultColor()
                }
                appointmentResults.size > 1 -> {
                    // Multiple appointments.
                    dayColors[dayIndex] = R.color.white
                }
                else -> {
                    // No appointments, but more other data?
                    val nhdResults = realm.getNhdObjects(dayDate)
                    val visusResults = realm.getVisusObjects(dayDate)
                    val readingTestResults = realm.getReadingTestObjects(dayDate)
                    val amslerTestResults = realm.getAmslerTestObjects(dayDate)
                    val noteResults = realm.getNoteObjects(dayDate)

                    var noteContent = ""
                    noteResults?.let {
                        noteContent = it.first()?.content ?: ""
                    }

                    if (nhdResults != null ||
                            visusResults != null ||
                            readingTestResults != null ||
                            amslerTestResults != null ||
                            !noteContent.isEmpty()) {
                        dayColors[dayIndex] = R.color.white
                    }
                }
            }
        }

        // Prepare the model.
        val cellModel = CalendarWeekCellModel(
                largeStyle,  weekDate,
                dayTexts[0], dayColors[0],
                dayTexts[1], dayColors[1],
                dayTexts[2], dayColors[2],
                dayTexts[3], dayColors[3],
                dayTexts[4], dayColors[4],
                dayTexts[5], dayColors[5],
                dayTexts[6], dayColors[6],
                activity
        )
        val cellViewModel = CalendarWeekCellViewModel(cellModel)

        return arrayListOf(cellViewModel)
    }

    /**
     * Returns cell index for the given date in table.
     *
     * @param activity: The activity to present the table.
     * @param date: The focus date to show.
     * @return: The index of the cell.
     */
    fun indexForDate(activity: CalendarSceneActivity, date: Date): Int {
        val tableCellData = mainCellsMutableData.value ?: ArrayList()
        if (tableCellData.size == 0) { return 0 }

        val calendar = Calendar.getInstance()
        calendar.time = date

        // Make month cell data with the given date.
        val monthValue = calendar.get(Calendar.MONTH)
        val yearValue = calendar.get(Calendar.YEAR)
        val monthType = CalendarMonth.getType(monthValue + 1)

        val content = activity.getString(monthType.titleString())+ " " + yearValue.toString()
        val element = tableCellData.single {
            it.cellType == ViewCellType.CalendarMonthCell && (it as CalendarMonthCellViewModel).cellTitle == content
        }

        return tableCellData.indexOf(element)
    }
}