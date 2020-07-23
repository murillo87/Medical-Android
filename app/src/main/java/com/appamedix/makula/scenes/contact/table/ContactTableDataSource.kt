package com.appamedix.makula.scenes.contact.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.contact.ContactDisplayModel
import com.appamedix.makula.scenes.contact.table.maincell.ContactCellModel
import com.appamedix.makula.scenes.contact.table.maincell.ContactCellViewModel
import com.appamedix.makula.types.ContactType
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel

class ContactTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: ContactDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add the main contact cells.
        val contactCells = createContactCellData(displayModel)
        contactCells?.let { tableData += it }

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: ContactDisplayModel): List<NavigationCellViewModel> {
        val title = displayModel.activity.getString(R.string.contactTitle)
        val navigationCellModel = NavigationCellModel(
                title,
                R.color.white,
                true,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Add,
                displayModel.activity
        )
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    /**
     * Creates a contact cell data.
     *
     * @param displayModel: The display model.
     * @return The list of contact cell view models.
     */
    private fun createContactCellData(displayModel: ContactDisplayModel): List<ContactCellViewModel>? {
        val cellData = displayModel.contactObjects

        if (cellData.size == 0) return null
        else return cellData.map {
            val title: String
            val editable: Boolean

            when (it.getContactType()) {
                ContactType.Custom -> {
                    title = if (it.name == null) displayModel.activity.getString(R.string.unnamedContact) else it.name ?: ""
                    editable = true
                }
                else -> {
                    title = displayModel.activity.getString(it.getContactType().displayString())
                    editable = false
                }
            }
            ContactCellViewModel(
                    ContactCellModel(
                            title,
                            it.getContactType().defaultColor(),
                            it.getContactType().highlightColor(),
                            editable,
                            displayModel.largeStyle,
                            displayModel.activity
                    )
            )
        }
    }
}