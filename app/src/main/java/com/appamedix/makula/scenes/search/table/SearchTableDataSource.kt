package com.appamedix.makula.scenes.search.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.scenes.search.SearchDisplayModel
import com.appamedix.makula.scenes.search.table.inputcell.SearchInputCellModel
import com.appamedix.makula.scenes.search.table.inputcell.SearchInputCellViewModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.InfoType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellModel
import com.appamedix.makula.views.textcell.StaticTextCellViewModel

class SearchTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    var infoEntries: List<InfoType> = listOf()

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: SearchDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add the input cell data.
        val inputCell = createInputCellData(displayModel)
        tableData += inputCell

        // Add the cell data for the search result.
        if (!displayModel.searchString.isEmpty()) {
            val resultCells = createResultsCellData(displayModel)
            tableData += resultCells
        }

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: SearchDisplayModel): List<NavigationCellViewModel> {
        val title = displayModel.activity.getString(R.string.searchTitle)
        val navigationCellModel = NavigationCellModel(
                title,
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

    /**
     * Creates an input cell data.
     *
     * @param displayModel: The display model.
     * @return The input cell view model.
     */
    private fun createInputCellData(displayModel: SearchDisplayModel): List<SearchInputCellViewModel> {
        val inputCellModel = SearchInputCellModel(
                displayModel.largeStyle,
                displayModel.searchString,
                displayModel.activity)
        val inputCellViewModel = SearchInputCellViewModel(inputCellModel)

        return arrayListOf(inputCellViewModel)
    }

    /**
     * Creates cell data for the search result.
     *
     * @param displayModel: The display model.
     * @return The text cell data.
     */
    private fun createResultsCellData(displayModel: SearchDisplayModel): List<StaticTextCellViewModel> {
        // Get all entries matching the search.
        val allInfoEntries = InfoType.values()
        val filteredEntries = allInfoEntries.filter {
            val title = displayModel.activity.getString(it.titleString()).toLowerCase()
            val content = displayModel.activity.getString(it.contentString()).toLowerCase()
            title.contains(displayModel.searchString.toLowerCase())
                    || content.contains(displayModel.searchString.toLowerCase())
        }
        infoEntries = filteredEntries.sortedWith(compareBy {it.titleString()})

        val cellModels = infoEntries.map {
            val cellTitle = displayModel.activity.getString(it.titleString())
            StaticTextCellModel(
                    MenuCellIdentifier.NoMenu,
                    SceneId.Info,
                    cellTitle,
                    null,
                    displayModel.largeStyle,
                    R.color.darkMain,
                    R.color.white,
                    R.color.lightMain,
                    true,
                    R.color.darkMain,
                    R.color.white,
                    false
            )
        }

        return cellModels.map {
            StaticTextCellViewModel(it)
        }
    }
}