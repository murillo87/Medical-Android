package com.appamedix.makula.global.swipe

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class DividedItemDecoration : RecyclerView.ItemDecoration {
    private var mDivider: Drawable? = null
    private var mShowFirstDivider: Boolean = false
    private var mShowLastDivider: Boolean = false

    constructor(context: Context, attrs: AttributeSet) {
        val a: TypedArray = context.obtainStyledAttributes(attrs, IntArray(android.R.attr.listDivider))
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    constructor(context: Context, attrs: AttributeSet, showFirstDivider: Boolean, showLastDivider: Boolean) {
        DividedItemDecoration(context, attrs)
        mShowFirstDivider = showFirstDivider
        mShowLastDivider = showLastDivider
    }

    constructor(divider: Drawable) {
        mDivider = divider
    }

    constructor(divider: Drawable, showFirstDivider: Boolean, showLastDivider: Boolean) {
        DividedItemDecoration(divider)
        mShowFirstDivider = showFirstDivider
        mShowLastDivider = showLastDivider
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (mDivider == null) return
        if (parent.getChildAdapterPosition(view) < 1) return

        if (getOrientation(parent) == LinearLayout.VERTICAL) {
            outRect.top = mDivider!!.intrinsicHeight
        } else {
            outRect.top = mDivider!!.intrinsicWidth
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mDivider == null) {
            super.onDrawOver(c, parent, state)
            return
        }

        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
        val size: Int

        val orientation = getOrientation(parent)
        val childCount = parent.childCount

        if (orientation == LinearLayoutManager.VERTICAL) {
            size = mDivider!!.intrinsicHeight
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
        } else {
            size = mDivider!!.intrinsicWidth
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
        }

        val startIdx = if (mShowFirstDivider) 0 else 1
        for (i in startIdx until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams

            if (orientation == LinearLayoutManager.VERTICAL) {
                top = child.top - params.topMargin
                bottom = top + size
            } else {
                left = child.left - params.leftMargin
                right = left + size
            }
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }

        if (mShowLastDivider && childCount > 0) {
            val child = parent.getChildAt(childCount - 1)
            val params = child.layoutParams as RecyclerView.LayoutParams

            if (orientation == LinearLayoutManager.VERTICAL) {
                top = child.bottom + params.bottomMargin
                bottom = top + size
            } else {
                left = child.right + params.rightMargin
                right = left + size
            }
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c)
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            return layoutManager.orientation
        } else {
            throw IllegalStateException("Invalid layout")
        }
    }
}