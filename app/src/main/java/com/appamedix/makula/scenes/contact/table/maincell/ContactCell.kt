package com.appamedix.makula.scenes.contact.table.maincell

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.appamedix.makula.R
import com.appamedix.makula.constants.Const
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.utils.Run
import com.daimajia.swipe.SwipeLayout

/* The custom class for the contact cell view */
class ContactCell(private val context: Context, view: View)
    : RecyclerView.ViewHolder(view), View.OnClickListener {

    // The UI elements to consist this cell view.
    val swipeLayout: SwipeLayout = view.findViewById(R.id.swipe_contact)
    val leftDelete: TextView = view.findViewById(R.id.left_delete)
    val rightDelete: TextView = view.findViewById(R.id.right_delete)

    private val leftMenu: LinearLayout = view.findViewById(R.id.left_menu)
    private val rightMenu: LinearLayout = view.findViewById(R.id.right_menu)
    private val contentTitle: TextView = view.findViewById(R.id.contact_cell_title)
    private val separator: View = view.findViewById(R.id.contact_cell_separator)
    private val dragButton: ImageButton = view.findViewById(R.id.contact_drag_button)

    private lateinit var cellModel: ContactCellViewModel
    private var clickListener: CellItemListener? = null

    fun bindItems(model: ContactCellViewModel, clickListener: CellItemListener?) {
        // Apply model and listener.
        this.cellModel = model
        this.clickListener = clickListener

        // Setup views
        val defaultColor = ContextCompat.getColor(context, model.defaultColor)
        contentTitle.text = model.title
        contentTitle.setTextColor(defaultColor)
        separator.setBackgroundColor(defaultColor)
        dragButton.visibility = if (model.editable) View.VISIBLE else View.GONE

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
        clickListener?.let {
            swipeLayout.close()
            applyHighlightColor()
            Run.after(Const.Time.defaultAnimationDuration) {
                applyDefaultColor()
            }
            it.onItemClicked(cellModel, ViewCellType.ContactCell)
        }
    }

    /**
     * Sets the color of the views depending on the current style state.
     */
    private fun applyDefaultColor() {
        val defaultColor = ContextCompat.getColor(context, cellModel.defaultColor)
        contentTitle.setTextColor(defaultColor)
        separator.setBackgroundColor(defaultColor)
    }

    /**
     * Sets the color of the views for the selected state.
     */
    private fun applyHighlightColor() {
        val highlightColor = ContextCompat.getColor(context, cellModel.highlightColor)
        contentTitle.setTextColor(highlightColor)
        separator.setBackgroundColor(highlightColor)
    }
}