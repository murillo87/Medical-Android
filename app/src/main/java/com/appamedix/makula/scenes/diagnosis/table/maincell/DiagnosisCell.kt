package com.appamedix.makula.scenes.diagnosis.table.maincell

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const

/* The custom class for the diagnosis cell. */
class DiagnosisCell(private val context: Context, private val view: View)
    : RecyclerView.ViewHolder(view) {

    // The UI elements for this cell.
    private val titleLabel: TextView = view.findViewById(R.id.diagnosis_cell_title)
    private val subtitleLabel: TextView = view.findViewById(R.id.diagnosis_cell_subtitle)
    private val radioIcon: ImageView = view.findViewById(R.id.diagnosis_cell_radio)
    private val infoButton: ImageButton = view.findViewById(R.id.diagnosis_cell_info)
    private val separator: View = view.findViewById(R.id.diagnosis_cell_separator)

    // The listener for the cell actions.
    private var listener: DiagnosisCellListener? = null

    // The index of this cell.
    private var cellIndex: Int = -1

    // Whether this cell is selected or not.
    var selected: Boolean = false

    /**
     * Sets up the cell view.
     *
     * @param model: The cell's view model.
     * @param position: The cell's index.
     */
    fun bindItems(model: DiagnosisCellViewModel, position: Int) {
        // Apply view model.
        titleLabel.text = model.title
        subtitleLabel.text = model.subtitle
        radioIcon.scaleType = ImageView.ScaleType.FIT_START
        infoButton.scaleType = ImageView.ScaleType.FIT_END
        listener = model.listener
        cellIndex = position
        selected = model.selected

        if (selected) applyHighlightColor()
        else applyDefaultColor()

        // Register for callback listeners.
        infoButton.setOnClickListener {
            listener?.infoButtonPressed(model.diagnosisType)
        }
        view.setOnClickListener {
            listener?.cellItemClicked(cellIndex)
        }

        // Adjust the cell view elements in the case of large style, e.g. landscape mode.
        if (model.largeStyle) {
            // Enlarge the text size.
            titleLabel.textSize = Const.Font.headlineLarge
            subtitleLabel.textSize = Const.Font.headlineLarge

            var height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    (Const.Size.radioButtonHeightNormal - 20) * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            radioIcon.layoutParams.width = height
            radioIcon.layoutParams.height = height
            infoButton.layoutParams.width = height
            infoButton.layoutParams.height = height

            // Enlarge the height of separator.
            height = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = height
        }
    }

    /**
     * Sets the color of the views depending on the current style state.
     */
    fun applyDefaultColor() {
        titleLabel.setTextColor(ContextCompat.getColor(context, R.color.lightMain))
        subtitleLabel.setTextColor(ContextCompat.getColor(context, R.color.lightMain))
        radioIcon.isActivated = false
        radioIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightMain), PorterDuff.Mode.SRC_IN)
    }

    /**
     * Sets the color of the views for the selected state.
     */
    fun applyHighlightColor() {
        titleLabel.setTextColor(ContextCompat.getColor(context, R.color.magenta))
        subtitleLabel.setTextColor(ContextCompat.getColor(context, R.color.magenta))
        radioIcon.isActivated = true
        radioIcon.setColorFilter(ContextCompat.getColor(context, R.color.magenta), PorterDuff.Mode.SRC_IN)
    }

    /**
     * Highlights the text while speaking.
     */
    fun applySpeechHighlightColor() {
        titleLabel.setTextColor(ContextCompat.getColor(context, R.color.white))
    }
}