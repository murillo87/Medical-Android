package com.appamedix.makula.scenes.contactdetail.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.contactdetail.ContactDetailDisplayModel
import com.appamedix.makula.scenes.contactdetail.ContactInfoType
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCellModel
import com.appamedix.makula.scenes.contactdetail.table.maincell.ContactDetailCellViewModel
import com.appamedix.makula.scenes.menu.table.MenuCellIdentifier
import com.appamedix.makula.types.ContactType
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellModel
import com.appamedix.makula.views.textcell.StaticTextCellViewModel
import com.appamedix.makula.worker.speech.SpeechSynthesizer

class ContactDetailTableDataSource : ViewModel() {

    private var mainCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    private var menuCellsSpeechData = MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>>()

    private val contactInfoOrder = arrayListOf(
            ContactInfoType.Name,
            ContactInfoType.Mobile,
            ContactInfoType.Phone,
            ContactInfoType.Email,
            ContactInfoType.Web,
            ContactInfoType.Street,
            ContactInfoType.City)

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMainCellData(displayModel: ContactDetailDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableData += navigation
        }

        // Add a static text cell to show the contact type if it's pre-defined type.
        val contactType = displayModel.contactObject.getContactType()
        if (contactType != ContactType.Custom && contactType != ContactType.AmdNet) {
            val textCell = createTextCellData(displayModel)
            tableData += textCell
        }

        // Add the main cells for the contact info types.
        for (infoType in contactInfoOrder) {
            val entryCell = createEntryCellData(displayModel, infoType)
            tableData += entryCell
        }

        mainCellsMutableData.value = ArrayList(tableData)
        return mainCellsMutableData
    }

    /**
     * Returns a mutable live data for the speech.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the speech.
     */
    fun getMenuCellSpeechData(displayModel: ContactDetailDisplayModel)
            : MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>> {
        val activity = displayModel.activity

        // Initialize data.
        var tableSpeechData: List<SpeechSynthesizer.SpeechData> = listOf()

        // Make speech data.
        var index = if (displayModel.largeStyle) 0 else -1
        var speechText: String
        val contactType = displayModel.contactObject.getContactType()
        if (contactType != ContactType.Custom && contactType != ContactType.AmdNet) {
            index += 1
            speechText = activity.getString(contactType.speechString())
            val textSpeech = SpeechSynthesizer.SpeechData(speechText, index)
            tableSpeechData += arrayListOf(textSpeech)
        }

        val speechData = contactInfoOrder.map {
            index += 1
            speechText = when (it) {
                ContactInfoType.Name -> {
                    displayModel.contactObject.name ?: activity.getString(it.speechString())
                }
                ContactInfoType.Mobile -> {
                    displayModel.contactObject.mobile ?: activity.getString(it.speechString())
                }
                ContactInfoType.Phone -> {
                    displayModel.contactObject.phone ?: activity.getString(it.speechString())
                }
                ContactInfoType.Email -> {
                    displayModel.contactObject.email ?: activity.getString(it.speechString())
                }
                ContactInfoType.Web -> {
                    displayModel.contactObject.web ?: activity.getString(it.speechString())
                }
                ContactInfoType.Street -> {
                    displayModel.contactObject.street ?: activity.getString(it.speechString())
                }
                ContactInfoType.City -> {
                    displayModel.contactObject.city ?: activity.getString(it.speechString())
                }
            }

            SpeechSynthesizer.SpeechData(speechText, index)
        }
        tableSpeechData += speechData

        menuCellsSpeechData.value = ArrayList(tableSpeechData)
        return menuCellsSpeechData
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: ContactDetailDisplayModel): List<NavigationCellViewModel> {
        val title = displayModel.activity.getString(R.string.contactDetailTitle)
        val navigationCellModel = NavigationCellModel(
                title,
                R.color.white,
                true,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Speaker,
                displayModel.activity
        )
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    /**
     * Creates a static text cell data.
     *
     * @param displayModel: The display model.
     * @return A static cell view model.
     */
    private fun createTextCellData(displayModel: ContactDetailDisplayModel): List<StaticTextCellViewModel> {
        val contactType = displayModel.contactObject.getContactType()
        val title = displayModel.activity.getString(contactType.displayString())
        val textCellModel = StaticTextCellModel(
                MenuCellIdentifier.NoMenu,
                SceneId.Other, title,
                null,
                displayModel.largeStyle,
                contactType.defaultColor(),
                contactType.highlightColor(),
                R.color.darkMain,
                true,
                null,
                null,
                true
        )
        val textCellViewModel = StaticTextCellViewModel(textCellModel)

        return arrayListOf(textCellViewModel)
    }

    /**
     * Creates an entry cell data for the contact detail.
     *
     * @param displayModel: The display model.
     * @param infoType: The contact info type for the cell.
     * @return A contact detail cell view model.
     */
    private fun createEntryCellData(displayModel: ContactDetailDisplayModel, infoType: ContactInfoType): List<ContactDetailCellViewModel> {
        val contactObject = displayModel.contactObject
        val actable: Boolean
        val title: String?

        when (infoType) {
            ContactInfoType.Name -> {
                title = contactObject.name
                actable = false
            }
            ContactInfoType.Mobile -> {
                title = contactObject.mobile
                actable = !title.isNullOrEmpty()
            }
            ContactInfoType.Phone -> {
                title = contactObject.phone
                actable = !title.isNullOrEmpty()
            }
            ContactInfoType.Email -> {
                title = contactObject.email
                actable = !title.isNullOrEmpty()
            }
            ContactInfoType.Web -> {
                title = contactObject.web
                actable = !title.isNullOrEmpty()
            }
            ContactInfoType.Street -> {
                title = contactObject.street
                actable = false
            }
            ContactInfoType.City -> {
                title = contactObject.city
                actable = false
            }
        }
        val editable = if (contactObject.getContactType() == ContactType.AmdNet) false else !title.isNullOrEmpty()

        val cellModel = ContactDetailCellModel(
                infoType,
                title,
                contactObject.getContactType().defaultColor(),
                contactObject.getContactType().highlightColor(),
                editable,
                actable,
                displayModel.largeStyle
        )
        val cellViewModel = ContactDetailCellViewModel(cellModel)

        return arrayListOf(cellViewModel)
    }
}