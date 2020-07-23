package com.appamedix.makula.scenes.readingtest.table.readingcell

import android.content.Context
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

/* The custom class for the reading test cell. */
class ReadingTestCell(private val context: Context, view: View) : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell.
    private val leftRadioImage: ImageView = view.findViewById(R.id.reading_test_left_radio)
    private val rightRadioImage: ImageView = view.findViewById(R.id.reading_test_right_radio)
    private val leftButton: Button = view.findViewById(R.id.reading_test_left_button)
    private val rightButton: Button = view.findViewById(R.id.reading_test_right_button)
    private val content: TextView = view.findViewById(R.id.reading_test_content)
    private val separator: View = view.findViewById(R.id.reading_test_cell_separator)

    // The listener for the left and right buttons.
    private var listener: ReadingTestCellListener? = null

    // The index of this cell.
    private var cellIndex: Int = -1

    /**
     * Sets up the cell view.
     *
     * @param model: The cell's view model.
     * @param position: The cell's index.
     */
    fun bindItems(model: ReadingTestCellViewModel, position: Int) {
        // Apply view model.
        content.text = model.content
        content.textSize = model.magnitudeType.textFontSize()
        content.maxLines = model.magnitudeType.defaultLines()
        listener = model.listener
        cellIndex = position

        // Update the width of left & right buttons.
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        leftButton.layoutParams.width = screenWidth / 2
        rightButton.layoutParams.width = screenWidth / 2

        leftRadioImage.isActivated = model.leftSelected
        rightRadioImage.isActivated = model.rightSelected
        leftRadioImage.scaleType = ImageView.ScaleType.FIT_START
        rightRadioImage.scaleType = ImageView.ScaleType.FIT_END

        // Register for callback listeners.
        leftButton.setOnClickListener {
            leftRadioImage.isActivated = !leftRadioImage.isActivated
            listener?.leftButtonSelected(cellIndex)
        }
        rightButton.setOnClickListener {
            rightRadioImage.isActivated = !rightRadioImage.isActivated
            listener?.rightButtonSelected(cellIndex)
        }

        // Adjust the cell view elements in the case of large style, e.g. landscape mode.
        if (model.largeStyle) {
            // Enlarge the size of radio images.
            var height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    (Const.Size.radioButtonHeightNormal - 20) * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftRadioImage.layoutParams.width = height
            leftRadioImage.layoutParams.height = height
            rightRadioImage.layoutParams.width = height
            rightRadioImage.layoutParams.height = height

            // Enlarge the height of the touch buttons.
            height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.radioButtonHeightNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftButton.layoutParams.height = height
            rightButton.layoutParams.height = height

            // Enlarge the height of separator.
            height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }
}