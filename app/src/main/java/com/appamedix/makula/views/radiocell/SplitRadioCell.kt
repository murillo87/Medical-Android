package com.appamedix.makula.views.radiocell

import android.content.Context
import android.content.res.Resources
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

/* The custom class for the split radio cell. */
class SplitRadioCell(private val context: Context, view: View) : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell.
    private val leftButton: ImageButton = view.findViewById(R.id.split_radio_left_button)
    private val rightButton: ImageButton = view.findViewById(R.id.split_radio_right_button)
    private val title: TextView = view.findViewById(R.id.split_radio_title)
    private val separator: View = view.findViewById(R.id.split_radio_cell_separator)

    // The listener for the left and right buttons.
    private var listener: SplitRadioCellListener? = null

    // The index of this cell.
    private var cellIndex: Int = -1

    /**
     * Sets up the cell view.
     *
     * @param model: The cell's view model.
     * @param position: The cell's index.
     */
    fun bindItems(model: SplitRadioCellViewModel, position: Int) {
        // Apply view model.
        title.text = model.title
        listener = model.listener
        cellIndex = position

        // Update the width of left & right buttons.
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        leftButton.layoutParams.width = screenWidth / 2
        rightButton.layoutParams.width = screenWidth / 2

        leftButton.isActivated = model.leftSelected
        rightButton.isActivated = model.rightSelected
        leftButton.scaleType = ImageView.ScaleType.FIT_START
        rightButton.scaleType = ImageView.ScaleType.FIT_END

        // Register for callback listeners.
        if (!model.disabled) {
            leftButton.setOnClickListener {
                leftButton.isActivated = !leftButton.isActivated
                listener?.leftButtonSelected(cellIndex)
            }
            rightButton.setOnClickListener {
                rightButton.isActivated = !rightButton.isActivated
                listener?.rightButtonSelected(cellIndex)
            }
        }

        // Adjust the cell view elements in the case of large style, e.g. landscape mode.
        if (model.largeStyle) {
            // Enlarge the title size.
            title.textSize = Const.Font.headlineLarge

            // Enlarge the size of radio buttons.
            var height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.radioButtonHeightNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftButton.layoutParams.height = height
            rightButton.layoutParams.height = height

            // Adjust the top and bottom padding of radio buttons.
            val padding = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.radioButtonPaddingNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftButton.setPadding(leftButton.paddingLeft, padding, leftButton.paddingRight, padding)
            rightButton.setPadding(rightButton.paddingLeft, padding, rightButton.paddingRight, padding)

            // Enlarge the height of separator.
            height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }

    /**
     * Sets the text color as original.
     */
    fun applySpeechDefaultColor() {
        title.setTextColor(ContextCompat.getColor(context, R.color.lightMain))
    }

    /**
     * Highlights the text while speaking.
     */
    fun applySpeechHighlightColor() {
        title.setTextColor(ContextCompat.getColor(context, R.color.white))
    }
}