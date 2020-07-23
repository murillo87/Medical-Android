package com.appamedix.makula.scenes.medicament.table

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.medicament.table.inputcell.MedicamentInputCell
import com.appamedix.makula.scenes.medicament.table.inputcell.MedicamentInputCellViewModel
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCell
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCellListener
import com.appamedix.makula.scenes.medicament.table.maincell.MedicamentCellViewModel
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCell
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter

class MedicamentTableAdapter(private val context: Context,
                             val arrayList: ArrayList<BaseCellViewModel>)
    : RecyclerSwipeAdapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_NAVIGATION = 0
        const val TYPE_INPUT_CELL = 1
        const val TYPE_MEDICAMENT_CELL = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (arrayList[position].cellType) {
            ViewCellType.Navigation -> TYPE_NAVIGATION
            ViewCellType.MedicamentInputCell -> TYPE_INPUT_CELL
            ViewCellType.MedicamentCell -> TYPE_MEDICAMENT_CELL
            else -> throw IllegalArgumentException("Invalid cell type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = when (viewType) {
            TYPE_NAVIGATION -> layoutInflater.inflate(R.layout.navigation_view, parent, false)
            TYPE_INPUT_CELL -> layoutInflater.inflate(R.layout.text_input_cell, parent, false)
            TYPE_MEDICAMENT_CELL -> layoutInflater.inflate(R.layout.medicament_cell_view, parent, false)
            else -> throw IllegalArgumentException("Invalid cell type")
        }

        return when (viewType) {
            TYPE_MEDICAMENT_CELL -> MedicamentCell(context, view)
            TYPE_INPUT_CELL -> MedicamentInputCell(context, view)
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
            ViewCellType.MedicamentInputCell -> {
                (holder as MedicamentInputCell).bindItems(cellViewModel as MedicamentInputCellViewModel, position)
            }
            else -> {
                val viewHolder = holder as MedicamentCell
                val model = cellViewModel as MedicamentCellViewModel
                viewHolder.bindItems(model, position)

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
    }

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_medicament
    }

    private fun removeCellItem(viewHolder: MedicamentCell, position: Int, listener: MedicamentCellListener?) {
        mItemManger.removeShownLayouts(viewHolder.swipeLayout)
        arrayList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, arrayList.size)
        mItemManger.closeAllItems()

        listener?.deleteButtonClicked(position)
    }
}