package com.appamedix.makula.scenes.contact.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.scenes.contact.table.maincell.ContactCell
import com.appamedix.makula.scenes.contact.table.maincell.ContactCellListener
import com.appamedix.makula.scenes.contact.table.maincell.ContactCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCell
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter

class ContactTableAdapter(private val context: Context,
                          private val arrayList: ArrayList<BaseCellViewModel>,
                          private val clickListener: CellItemListener)
    : RecyclerSwipeAdapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NAVIGATION = 0
        const val TYPE_CONTACT_CELL = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.Navigation -> TYPE_NAVIGATION
            ViewCellType.ContactCell -> TYPE_CONTACT_CELL
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_CONTACT_CELL -> layoutInflater.inflate(R.layout.contact_cell_view, parent, false)
            else -> layoutInflater.inflate(R.layout.navigation_view, parent, false)
        }

        return when (viewType) {
            TYPE_CONTACT_CELL -> ContactCell(context, view)
            else -> NavigationCell(context, view)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cellViewModel = arrayList[position]

        if (cellViewModel.cellType == ViewCellType.Navigation) {
            (holder as NavigationCell).bindItems(cellViewModel as NavigationCellViewModel)
        } else {
            val viewHolder = holder as ContactCell
            val model = cellViewModel as ContactCellViewModel
            viewHolder.bindItems(model, clickListener)

            viewHolder.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut
            if (!model.editable) {
                viewHolder.swipeLayout.isSwipeEnabled = false
            }

            // Add left menu
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left,
                    viewHolder.swipeLayout.findViewById(R.id.left_menu))
            // Add right menu
            viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right,
                    viewHolder.swipeLayout.findViewById(R.id.right_menu))

            viewHolder.leftDelete.setOnClickListener {
                removeCellItem(viewHolder, position, model.listener)
            }
            viewHolder.rightDelete.setOnClickListener {
                removeCellItem(viewHolder, position, model.listener)
            }

            mItemManger.bindView(viewHolder.itemView, position)
        }
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_contact
    }

    private fun removeCellItem(viewHolder: ContactCell, position: Int, listener: ContactCellListener?) {
        mItemManger.removeShownLayouts(viewHolder.swipeLayout)
        arrayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, arrayList.size)
        mItemManger.closeAllItems()

        listener?.deleteButtonClicked(position)
    }
}