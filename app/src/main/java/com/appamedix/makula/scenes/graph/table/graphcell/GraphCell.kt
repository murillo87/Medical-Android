package com.appamedix.makula.scenes.graph.table.graphcell

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.graph.EyeType
import com.appamedix.makula.scenes.visusnhd.VisusNhdInputConst
import com.appamedix.makula.utils.DateUtils
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView
import java.util.*
import kotlin.collections.ArrayList

class GraphCell(private val context: Context, view: View) : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell.
    private val leftTitle: TextView = view.findViewById(R.id.left_title)
    private val rightTitle: TextView = view.findViewById(R.id.right_title)
    private val chart: LineChartView = view.findViewById(R.id.chart)

    private lateinit var cellModel: GraphCellViewModel
    private var lineChartData: LineChartData = LineChartData()
    private val chartLines: ArrayList<Line> = ArrayList()

    /**
     * Sets up the cell view.
     *
     * @param model: The cell's view model.
     */
    fun bindItems(model: GraphCellViewModel) {
        this.cellModel = model

        // Adjust the cell view elements in the case of large style, e.g. landscape mode.
        if (model.largeStyle) {
            // Enlarge the title size.
            leftTitle.textSize = Const.Font.content1Large
            rightTitle.textSize = Const.Font.content1Large
        }

        // Draw graph.
        generateGraph()
    }

    /**
     * Draws the graph lines with Visus and NHD data.
     */
    private fun generateGraph() {
        // Sets up graph range.
        val rangeLine = setupGraphRange()
        chartLines.add(rangeLine)

        // Add a line for each treatment date.
        val redLines = drawTreatmentRedLines()
        chartLines.addAll(redLines)

        // Add a line with Visus data.
        val visusLine = drawVisusDataLine()
        visusLine?.let { chartLines.add(it) }

        // Add a line with NHD data.
        val nhdLine = drawNhdDataLine()
        nhdLine?.let { chartLines.add(it) }

        // Set data and axes.
        lineChartData = LineChartData(chartLines)

        // Sets up top axis to avoid side axis labels cut off.
        // Use transparent, so it doesn't show.
        val topAxis = createTopAxis()
        lineChartData.axisXTop = topAxis

        // Sets up bottom axis.
        val bottomAxis = createBottomAxis()
        lineChartData.axisXBottom = bottomAxis

        // Sets up left vertical axis.
        val leftAxis = createNhdAxis()
        lineChartData.axisYLeft = leftAxis

        // Sets up right vertical axis.
        val rightAxis = createVisusAxis()
        lineChartData.axisYRight = rightAxis

        // Set data in chart view.
        chart.lineChartData = lineChartData

        // Adjust the visible area of view port to make it scrollable in horizontally.
        val visiblePortion = 1.0f - Const.Graph.xAxisStepsPerPage / Const.Graph.xAxisSteps
        val viewPort = Viewport(chart.maximumViewport)
        viewPort.set(viewPort.right * visiblePortion, viewPort.top, viewPort.right, viewPort.bottom)
        chart.currentViewport = viewPort
        chart.isZoomEnabled = false
    }

    /**
     * Transforms a date into a float representing the position on the X axis of the graph.
     *
     * The X axis is defined from right to left with the current's date next month on the right as last value.
     * So when today is the 15th of June, the end month on the right of the graph is 1st of July which has the X-axis max value, e.g. `24`.
     * June then is represented by `23`, May as `22`, etc, until `0`.
     * The month's day is then transformed into a fraction of the month, e.g. the 15th is `0.5` which then gets added to the index.
     * June the 15th then would be transformed into `23.5` that way.
     * June the 1st is then `23.0` and July the 1st `24.0`.
     *
     * @param date: The date on the X axis.
     * @return The date's representation value as a float.
     */
    private fun xAxisValueForDate(date: Date): Float {
        val dateTruncated = DateUtils.truncateTime(date)
        val endDate = DateUtils.increaseMonth(DateUtils.startOfMonth(Date()), 1)
        val monthsDiff = DateUtils.monthDiffBetweenDates(dateTruncated, endDate)
        val daysInMonth = DateUtils.getMaxDaysInMonth(dateTruncated)
        val dayNumber = DateUtils.getDay(dateTruncated)
        val dayFraction = (dayNumber - 1).toFloat() / daysInMonth.toFloat()

        return Const.Graph.xAxisSteps - monthsDiff + dayFraction
    }

    /**
     * Sets up the graph range (X-range, Y-range) by setting data at start and end points.
     * The line is drawn from (0, 0) to (X-max, Y-max), but it's not shown.
     *
     * @return The transparent graph line.
     */
    private fun setupGraphRange(): Line {
        val values = ArrayList<PointValue>()

        values.add(PointValue(0f, 0f))
        values.add(PointValue(Const.Graph.xAxisSteps, Const.Graph.yAxisSteps))

        val line = Line(values)
        line.color = ContextCompat.getColor(context, R.color.transparent)
        line.setHasPoints(false)
        line.strokeWidth = 0

        return line
    }

    /**
     * Draws a red line at each treatment date.
     *
     * @return A list of red lines.
     */
    private fun drawTreatmentRedLines(): ArrayList<Line> {
        if (cellModel.ivomDates.isEmpty()) return ArrayList()

        val lines = ArrayList<Line>()
        for (ivomDate in cellModel.ivomDates) {
            val xVal = xAxisValueForDate(ivomDate)
            val values = ArrayList<PointValue>()
            values.add(PointValue(xVal, 0f))
            values.add(PointValue(xVal, Const.Graph.yAxisSteps))

            val line = Line(values)
            line.color = ContextCompat.getColor(context, R.color.magenta)
            line.setHasPoints(false)
            line.strokeWidth = Const.Graph.lineWidth
            lines.add(line)
        }

        return lines
    }

    /**
     * Draws a graph line for the Visus objects in green color.
     *
     * @return The line if the Visus objects exist, otherwise null.
     */
    private fun drawVisusDataLine(): Line? {
        if (cellModel.visusObjects.isEmpty()) return null

        val values = ArrayList<PointValue>()
        val visusValues = cellModel.visusObjects.map {
            if (cellModel.eyeType == EyeType.Left)
                it.valueLeft.toFloat()
            else
                it.valueRight.toFloat()
        }
        val visusPoints = cellModel.visusObjects.map {
            xAxisValueForDate(it.date)
        }

        for (i in 0 until visusValues.size) {
            values.add(PointValue(visusPoints[i], visusValues[i]))
        }

        val line = Line(values)
        line.color = ContextCompat.getColor(context, R.color.green)
        line.setHasPoints(true)
        line.strokeWidth = Const.Graph.lineWidth

        return line
    }

    /**
     * Draws a graph line for the NHD objects in green color.
     *
     * @return The line if the NHD objects exist, otherwise null.
     */
    private fun drawNhdDataLine(): Line? {
        if (cellModel.nhdObjects.isEmpty()) return null

        val values = ArrayList<PointValue>()
        val nhdValues = cellModel.nhdObjects.map {
            if (cellModel.eyeType == EyeType.Left)
                it.valueLeft
            else
                it.valueRight
        }
        val nhdPoints = cellModel.nhdObjects.map {
            xAxisValueForDate(it.date)
        }

        val maxNhd = Const.Data.nhdMaxValue.toFloat()
        val minNhd = Const.Data.nhdMinValue.toFloat()
        for (i in 0 until nhdValues.size) {
            val realValue = (maxNhd - nhdValues[i]) / (maxNhd - minNhd) * Const.Graph.yAxisSteps
            values.add(PointValue(nhdPoints[i], realValue))
        }

        val line = Line(values)
        line.color = ContextCompat.getColor(context, R.color.yellow)
        line.setHasPoints(true)
        line.strokeWidth = Const.Graph.lineWidth

        return line
    }

    /* Graph axes for bottom, vertical left and right */

    private fun createTopAxis(transparent: Boolean = true): Axis {
        // Calculate values for top X-Axis.
        val axisValues = ArrayList<AxisValue>()
        val maxXValue = Const.Graph.xAxisSteps.toInt()
        for (x in 0..maxXValue) {
            axisValues.add(AxisValue(x.toFloat()).setLabel(x.toString()))
        }

        // Make axis for bottom.
        return Axis(axisValues)
                .setHasLines(true)
                .setTextColor(ContextCompat.getColor(context, if (transparent) R.color.transparent else R.color.white))
                .setTextSize(Const.Font.graphNumbersDefault.toInt())
                .setFormatter(SimpleAxisValueFormatter())
    }

    private fun createBottomAxis(): Axis {
        // Calculate values for bottom X-Axis.
        val axisValues = ArrayList<AxisValue>()
        val today = Date()
        val maxXValue = Const.Graph.xAxisSteps.toInt()
        for (x in 0..maxXValue) {
            val diffMonth = x - (maxXValue - 1)
            val realDate = DateUtils.increaseMonth(today, diffMonth)
            val axisLabel = DateUtils.toSimpleString(realDate, "MM.yy")
            axisValues.add(AxisValue(x.toFloat()).setLabel(axisLabel))
        }

        // Make axis for bottom.
        return Axis(axisValues)
                .setHasLines(true)
                .setTextColor(ContextCompat.getColor(context, R.color.white))
                .setTextSize(Const.Font.graphTextDefault.toInt())
                .setFormatter(SimpleAxisValueFormatter())
    }

    private fun createVisusAxis(): Axis {
        // Calculate the axis values of visus.
        val visusAxisValues = ArrayList<AxisValue>()
        val maxYValue = Const.Graph.yAxisSteps.toInt()
        for (y in 0..maxYValue) {
            val axisLabel = VisusNhdInputConst.visusValues[y]
            visusAxisValues.add(AxisValue(y.toFloat()).setLabel(axisLabel))
        }

        // Make axis for Visus.
        return Axis(visusAxisValues)
                .setHasLines(true)
                .setMaxLabelChars(4)
                .setTextColor(ContextCompat.getColor(context, R.color.green))
                .setTextSize(Const.Font.graphNumbersDefault.toInt())
                .setFormatter(SimpleAxisValueFormatter())
    }

    private fun createNhdAxis(): Axis {
        // Calculate the axis values of NHD.
        val nhdAxisValues = ArrayList<AxisValue>()
        val maxYValue = Const.Graph.yAxisSteps.toInt()
        val maxNhd = Const.Data.nhdMaxValue
        val minNhd = Const.Data.nhdMinValue
        for (y in 0..maxYValue ) {
            val axisValue = maxNhd - y * (maxNhd - minNhd) / maxYValue
            nhdAxisValues.add(AxisValue(y.toFloat()).setLabel(axisValue.toString()))
        }

        // Make axis for NHD.
        return Axis(nhdAxisValues)
                .setHasLines(true)
                .setMaxLabelChars(3)
                .setTextColor(ContextCompat.getColor(context, R.color.yellow))
                .setTextSize(Const.Font.graphNumbersDefault.toInt())
                .setFormatter(SimpleAxisValueFormatter())
    }
}