package com.appamedix.makula.scenes.medicament.table.maincell

import android.content.Context
import android.graphics.PorterDuff
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.daimajia.swipe.SwipeLayout

class MedicamentCell(private val context: Context, view: View)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    // The UI elements to consist this cell view.
    val swipeLayout: SwipeLayout = view.findViewById(R.id.swipe_medicament)
    val leftDelete: TextView = view.findViewById(R.id.left_delete)
    val rightDelete: TextView = view.findViewById(R.id.right_delete)

    private val leftMenu: LinearLayout = view.findViewById(R.id.left_menu)
    private val rightMenu: LinearLayout = view.findViewById(R.id.right_menu)
    private val contentTitle: TextView = view.findViewById(R.id.medicament_cell_title)
    private val radioIcon: ImageView = view.findViewById(R.id.medicament_radio)
    private val separator: View = view.findViewById(R.id.medicament_cell_separator)
    private val dragButton: ImageButton = view.findViewById(R.id.medicament_drag)

    private lateinit var cellModel: MedicamentCellViewModel
    private var cellIndex: Int = -1
    private var listener: MedicamentCellListener? = null

    // Whether this cell is selected or not.
    var selected: Boolean = false

    fun bindItems(model: MedicamentCellViewModel, position: Int) {
        this.cellModel = model
        this.cellIndex = position
        this.listener = model.listener
        this.selected = model.selected

        // Setup views.
        contentTitle.text = model.title
        dragButton.scaleType = ImageView.ScaleType.FIT_CENTER
        radioIcon.scaleType = ImageView.ScaleType.FIT_END
        dragButton.visibility = if (model.editable) View.VISIBLE else View.GONE

        if (selected) applyHighlightColor()
        else applyDefaultColor()

        // Register for callback listeners.
        swipeLayout.surfaceView.setOnClickListener(this)
        dragButton.setOnClickListener {
            if (swipeLayout.openStatus == SwipeLayout.Status.Open)
                swipeLayout.close()
            else
                swipeLayout.open(SwipeLayout.DragEdge.Left)
        }

        // Adjust UI elements in the case of large style, e.g. landscape.
        if (model.largeStyle) {
            // Enlarge the text size of the title view.
            contentTitle.textSize = Const.Font.headlineLarge
            leftDelete.textSize = Const.Font.content1Large
            rightDelete.textSize = Const.Font.content1Large

            // Enlarge the drag button size, width is not changed.
            var sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.dragButtonHeightNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            dragButton.layoutParams.height = sizeValue

            // Enlarge the radio icon size.
            sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    (Const.Size.radioButtonHeightNormal - 20) * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            radioIcon.layoutParams.width = sizeValue
            radioIcon.layoutParams.height = sizeValue

            // Enlarge the size of swipe menu buttons.
            sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.swipeMenuWidthNormal * Const.Size.landscapeScaleFactor,
                    context.resources.displayMetrics).toInt()
            leftMenu.layoutParams.width = sizeValue
            rightMenu.layoutParams.width = sizeValue

            // Enlarge the height of separator.
            sizeValue = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    Const.Size.separatorThicknessLarge,
                    context.resources.displayMetrics).toInt()
            separator.layoutParams.height = sizeValue
        }
    }

    override fun onClick(v: View?) {
        if (cellModel.editable && (swipeLayout.openStatus == SwipeLayout.Status.Open)) {
            return
        }
        listener?.onCellItemClicked(cellIndex)
    }

    /**
     * Sets the color of the views depending on the current style state.
     */
    fun applyDefaultColor() {
        contentTitle.setTextColor(ContextCompat.getColor(context, R.color.lightMain))
        radioIcon.isActivated = false
        radioIcon.setColorFilter(ContextCompat.getColor(context, R.color.lightMain), PorterDuff.Mode.SRC_IN)
    }

    /**
     * Sets the color of the views for the selected state.
     */
    fun applyHighlightColor() {
        contentTitle.setTextColor(ContextCompat.getColor(context, R.color.magenta))
        radioIcon.isActivated = true
        radioIcon.setColorFilter(ContextCompat.getColor(context, R.color.magenta), PorterDuff.Mode.SRC_IN)
    }

    /**
     * Highlights the text while speaking.
     */
    fun applySpeechHighlightColor() {
        contentTitle.setTextColor(ContextCompat.getColor(context, R.color.white))
    }
}