package com.appamedix.makula.scenes.contactdetail.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCell
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCellListener
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCell
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellView
import com.appamedix.makula.views.textcell.StaticTextCellViewModel
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter

class ContactDetailTableAdapter(private val context: Context,
                                private val arrayList: ArrayList<BaseCellViewModel>,
                                private val listener: ContactDetailCellListener?)
    : RecyclerSwipeAdapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NAVIGATION = 0
        const val TYPE_STATIC_TEXT = 1
        const val TYPE_CONTACT_DETAIL = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.Navigation -> TYPE_NAVIGATION
            ViewCellType.StaticTextCell -> TYPE_STATIC_TEXT
            ViewCellType.ContactDetailCell -> TYPE_CONTACT_DETAIL
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_CONTACT_DETAIL -> layoutInflater.inflate(R.layout.contact_detail_cell, parent, false)
            TYPE_STATIC_TEXT -> layoutInflater.inflate(R.layout.static_text_cell, parent, false)
            else -> layoutInflater.inflate(R.layout.navigation_view, parent, false)
        }

        return when (viewType) {
            TYPE_CONTACT_DETAIL -> ContactDetailCell(context, view)
            TYPE_STATIC_TEXT -> StaticTextCellView(context, view)
            else -> NavigationCell(context, view)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val cellViewModel = arrayList[position]

        when (cellViewModel.cellType) {
            ViewCellType.Navigation -> {
                (holder as NavigationCell).bindItems(cellViewModel as NavigationCellViewModel)
            }
            ViewCellType.StaticTextCell -> {
                (holder as StaticTextCellView).bindItems(cellViewModel as StaticTextCellViewModel, null)
            }
            else -> {
                val viewHolder = holder as ContactDetailCell
                val model = cellViewModel as ContactDetailCellViewModel
                viewHolder.bindItems(model, listener)

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
                    viewHolder.contentEntry.text.clear()
                    listener?.deleteButtonClicked(model)
                }
                viewHolder.rightDelete.setOnClickListener {
                    viewHolder.contentEntry.text.clear()
                    listener?.deleteButtonClicked(model)
                }

                mItemManger.bindView(viewHolder.itemView, position)
            }
        }
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_contact_detail
    }
}