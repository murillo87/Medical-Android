package com.appamedix.makula.scenes.graph.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.graph.EyeType
import com.appamedix.makula.scenes.graph.GraphDisplayModel
import com.appamedix.makula.scenes.graph.table.graphcell.GraphCellModel
import com.appamedix.makula.scenes.graph.table.graphcell.GraphCellViewModel
import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.splitcell.SplitCellModel
import com.appamedix.makula.views.splitcell.SplitCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellModel
import com.appamedix.makula.views.textcell.StaticTextCellViewModel

class GraphTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: GraphDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add the split cell.
        val splitCell = createSplitCellData(displayModel)
        tableData += splitCell

        // Add the date title cell.
        val dateCell = createDateTitleCellData(displayModel)
        tableData += dateCell

        // Add the graph cell.
        val graphCell = createGraphCellData(displayModel)
        tableData += graphCell

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: GraphDisplayModel): List<NavigationCellViewModel> {
        // Get the cell's title.
        val cellTitle = displayModel.activity.getString(R.string.graphSceneTitle)

        // Prepare the cell's view model.
        val navigationCellModel = NavigationCellModel(
                cellTitle,
                R.color.white,
                true,
                true,
                true,
                ImageButtonType.Back,
                false,
                ImageButtonType.Speaker,
                displayModel.activity
        )
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    private fun createSplitCellData(displayModel: GraphDisplayModel): List<SplitCellViewModel> {
        val activity = displayModel.activity
        val leftMenuTitle = activity.getString(R.string.graphHeaderCellLeftEye)
        val rightMenuTitle = activity.getString(R.string.graphHeaderCellRightEye)

        val cellModel = SplitCellModel(
                leftMenuTitle,
                rightMenuTitle,
                null,
                displayModel.eyeType == EyeType.Left,
                displayModel.eyeType == EyeType.Right,
                displayModel.largeStyle,
                false,
                R.color.darkMain,
                R.color.lightMain,
                activity
        )
        val cellViewModel = SplitCellViewModel(cellModel)

        return arrayListOf(cellViewModel)
    }

    /**
     * Creates a static text cell data for a given date.
     *
     * @param displayModel: The display model.
     * @return The text cell view model.
     */
    private fun createDateTitleCellData(displayModel: GraphDisplayModel): List<StaticTextCellViewModel> {
        // Get the cell's title.
        val cellTitle = displayModel.activity.getString(R.string.graphIvomDateCellTitle)

        // Prepare the cell's view model.
        val cellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Other,
                cellTitle,
                null,
                displayModel.largeStyle,
                R.color.magenta,
                null,
                R.color.darkMain,
                true,
                R.color.lightMain,
                null,
                true
        )
        val cellViewModel = StaticTextCellViewModel(cellModel)

        return arrayListOf(cellViewModel)
    }

    /**
     * Creates a graph cell data.
     *
     * @param displayModel: The display model.
     * @return The graph cell view model.
     */
    private fun createGraphCellData(displayModel: GraphDisplayModel): List<GraphCellViewModel> {
        val cellModel = GraphCellModel(
                displayModel.largeStyle,
                displayModel.ivomDates,
                displayModel.eyeType,
                displayModel.visusObjects,
                displayModel.nhdObjects
        )
        val cellViewModel = GraphCellViewModel(cellModel)

        return arrayListOf(cellViewModel)
    }
}