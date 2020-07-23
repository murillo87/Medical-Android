package com.appamedix.makula.scenes.menu.table

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.appamedix.makula.BuildConfig
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.scenes.menu.MenuDisplayModel
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.textcell.StaticTextCellModel
import com.appamedix.makula.views.textcell.StaticTextCellViewModel
import com.appamedix.makula.worker.datamodel.appointment.getLastAppointments
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import io.realm.Realm
import java.util.*
import kotlin.collections.ArrayList

class MenuTableDataSource : ViewModel() {

    private var menuCellsMutableData = MutableLiveData<ArrayList<BaseCellViewModel>>()
    private var menuCellsSpeechData = MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>>()

    var realm: Realm? = null

    /**
     * Returns a mutable live data for the table cells.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the cells.
     */
    fun getMenuCellData(displayModel: MenuDisplayModel): MutableLiveData<ArrayList<BaseCellViewModel>> {
        // Initialize data.
        var tableCellData: List<BaseCellViewModel> = listOf()

        // Add the navigation view as first cell in large style.
        if (displayModel.largeStyle) {
            val navigation = createNavigationCellData(displayModel)
            tableCellData += navigation
        }

        // Add the main cells
        val mainCells = createMenuCellData(displayModel)
        tableCellData += mainCells

        menuCellsMutableData.value = ArrayList(tableCellData)
        return menuCellsMutableData
    }

    /**
     * Returns a mutable live data for the speech.
     *
     * @param displayModel: The display model.
     * @return A mutable live data list for the speech.
     */
    fun getMenuCellSpeechData(displayModel: MenuDisplayModel): MutableLiveData<ArrayList<SpeechSynthesizer.SpeechData>> {
        val activity = displayModel.activity
        val sceneId = displayModel.sceneId

        val tableData = setInitialTableData(sceneId)

        // Find the last appointments for the doctorVisit scene.
        val today = Date()
        val realm = realm ?: return menuCellsSpeechData
        val lastAppointments = realm.getLastAppointments(today)

        // Handle special case `treatment`.
        if (sceneId == SceneId.DoctorVisitMenu && lastAppointments.size == 0) {
            // No appointments found, remove the treatment cell.
            tableData.remove(MenuCellIdentifier.Treatment)
        }

        // Make speech data.
        var index = if (displayModel.largeStyle) 0 else -1
        val speechData = tableData.map {
            val text = when (it) {
                MenuCellIdentifier.Treatment -> {
                    val firstAppointment = lastAppointments.first()
                    val pattern = displayModel.activity.getString(R.string.commonSpeechDateFormat)
                    DateUtils.toSimpleString(firstAppointment.getAppointmentDate(), pattern)
                }
                MenuCellIdentifier.Version -> {
                    String.format(activity.getString(it.rawData().title), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
                }
                else -> activity.getString(it.speechText())
            }
            index += 1
            SpeechSynthesizer.SpeechData(text, index)
        }

        menuCellsSpeechData.value = ArrayList(speechData)
        return menuCellsSpeechData
    }

    /**
     * Make initial data for this scene.
     *
     * @param sceneId: The type of the menu scene.
     * @return The list of menu table data
     */
    private fun setInitialTableData(sceneId: SceneId): ArrayList<MenuCellIdentifier> {
        return when (sceneId) {
            SceneId.HomeMenu -> arrayListOf(
                    MenuCellIdentifier.DoctorVisit,
                    MenuCellIdentifier.NewAppointment,
                    MenuCellIdentifier.Calendar,
                    MenuCellIdentifier.ContactPerson,
                    MenuCellIdentifier.SelfTest,
                    MenuCellIdentifier.Knowledge,
                    MenuCellIdentifier.Addresses,
                    MenuCellIdentifier.News,
                    MenuCellIdentifier.Search,
                    MenuCellIdentifier.Settings,
                    MenuCellIdentifier.Manual,
                    MenuCellIdentifier.Inprint,
                    MenuCellIdentifier.Version
            )
            SceneId.DoctorVisitMenu -> arrayListOf(
                    MenuCellIdentifier.Treatment,
                    MenuCellIdentifier.Diagnosis,
                    MenuCellIdentifier.Medicament,
                    MenuCellIdentifier.VisusInput,
                    MenuCellIdentifier.NhdInput,
                    MenuCellIdentifier.OctVisus
            )
            SceneId.SelfTestMenu -> arrayListOf(
                    MenuCellIdentifier.AmslerTest,
                    MenuCellIdentifier.ReadingTest
            )
            SceneId.KnowledgeMenu -> arrayListOf(
                    MenuCellIdentifier.Illness,
                    MenuCellIdentifier.Examination,
                    MenuCellIdentifier.Therapy,
                    MenuCellIdentifier.Activities,
                    MenuCellIdentifier.Aid,
                    MenuCellIdentifier.Support,
                    MenuCellIdentifier.Diagnose
            )
            SceneId.SettingsMenu -> arrayListOf(
                    MenuCellIdentifier.Reminder,
                    MenuCellIdentifier.Backup
            )
            SceneId.IllnessMenu -> arrayListOf(
                    MenuCellIdentifier.IllnessInfo0,
                    MenuCellIdentifier.IllnessInfo4,
                    MenuCellIdentifier.IllnessInfo1,
                    MenuCellIdentifier.IllnessInfo2,
                    MenuCellIdentifier.IllnessInfo3,
                    MenuCellIdentifier.IllnessInfo5,
                    MenuCellIdentifier.IllnessInfo6,
                    MenuCellIdentifier.IllnessInfo7,
                    MenuCellIdentifier.IllnessInfo8,
                    MenuCellIdentifier.IllnessInfo9
            )
            SceneId.ExaminationMenu -> arrayListOf(
                    MenuCellIdentifier.ExaminationInfo0,
                    MenuCellIdentifier.ExaminationInfo1,
                    MenuCellIdentifier.ExaminationInfo2,
                    MenuCellIdentifier.ExaminationInfo3,
                    MenuCellIdentifier.ExaminationInfo5,
                    MenuCellIdentifier.ExaminationInfo4,
                    MenuCellIdentifier.ExaminationInfo6
            )
            SceneId.TherapyMenu -> arrayListOf(
                    MenuCellIdentifier.TherapyInfo0,
                    MenuCellIdentifier.TherapyInfo1,
                    MenuCellIdentifier.TherapyInfo2,
                    MenuCellIdentifier.TherapyInfo4,
                    MenuCellIdentifier.TherapyInfo3,
                    MenuCellIdentifier.TherapyInfo5
            )
            SceneId.ActivitiesMenu -> arrayListOf(
                    MenuCellIdentifier.ActivitiesInfo0,
                    MenuCellIdentifier.ActivitiesInfo1,
                    MenuCellIdentifier.ActivitiesInfo2,
                    MenuCellIdentifier.ActivitiesInfo3,
                    MenuCellIdentifier.ActivitiesInfo4,
                    MenuCellIdentifier.ActivitiesInfo5
            )
            SceneId.AidMenu -> arrayListOf(
                    MenuCellIdentifier.AidInfo7,
                    MenuCellIdentifier.AidInfo0,
                    MenuCellIdentifier.AidInfo1,
                    MenuCellIdentifier.AidInfo2,
                    MenuCellIdentifier.AidInfo3,
                    MenuCellIdentifier.AidInfo4,
                    MenuCellIdentifier.AidInfo5,
                    MenuCellIdentifier.AidInfo6
            )
            SceneId.SupportMenu -> arrayListOf(
                    MenuCellIdentifier.SupportInfo0,
                    MenuCellIdentifier.SupportInfo1,
                    MenuCellIdentifier.SupportInfo2,
                    MenuCellIdentifier.SupportInfo3,
                    MenuCellIdentifier.SupportInfo4
            )
            else -> ArrayList()
        }
    }

    /**
     * Creates a navigation cell data.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createNavigationCellData(displayModel: MenuDisplayModel): List<NavigationCellViewModel> {
        val activity = displayModel.activity
        val sceneId = displayModel.sceneId

        val navigationCellModel = NavigationCellModel(
                activity.getString(sceneId.titleString()),
                R.color.white,
                true,
                true,
                sceneId != SceneId.HomeMenu,
                ImageButtonType.Back,
                true,
                ImageButtonType.Speaker,
                activity)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)

        return arrayListOf(navigationCellViewModel)
    }

    /**
     * Creates a main cell data for the menu.
     *
     * @param displayModel: The display model.
     * @return The navigation view model.
     */
    private fun createMenuCellData(displayModel: MenuDisplayModel): List<StaticTextCellViewModel> {
        val activity = displayModel.activity
        val sceneId = displayModel.sceneId

        val tableData = setInitialTableData(sceneId)

        // Find the last appointments for the doctorVisit scene.
        val today = Date()
        val realm = realm ?: return listOf()
        val lastAppointments = realm.getLastAppointments(today)

        // Handle special case `treatment`.
        if (sceneId == SceneId.DoctorVisitMenu && lastAppointments.size == 0) {
            // No appointments found, remove the treatment cell.
            tableData.remove(MenuCellIdentifier.Treatment)
        }

        val menuCellModels = tableData.map {
            val title = when (it) {
                MenuCellIdentifier.Treatment -> {
                    val firstAppointment = lastAppointments.first()
                    DateUtils.toStringWithWeekday(displayModel.activity, firstAppointment.getAppointmentDate())
                }
                MenuCellIdentifier.Version -> {
                    String.format(activity.getString(it.rawData().title), BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE)
                }
                else -> activity.getString(it.rawData().title)
            }
            val defaultColor = when (it) {
                MenuCellIdentifier.Treatment -> {
                    if (lastAppointments.size > 1) R.color.white
                    else lastAppointments.first().getAppointmentType().defaultColor()
                }
                else -> {
                    if (it.rawData().darkStyle) R.color.lightMain else R.color.darkMain
                }
            }
            val highlightColor = when (it) {
                MenuCellIdentifier.Treatment -> {
                    if (lastAppointments.size > 1) R.color.lightMain
                    else lastAppointments.first().getAppointmentType().highlightColor()
                }
                else -> R.color.white
            }
            val separatorColor = when (it) {
                MenuCellIdentifier.Treatment -> {
                    if (it.rawData().darkStyle) R.color.lightMain else R.color.darkMain
                }
                else -> null
            }

            StaticTextCellModel(
                    it,
                    it.rawData().sceneId,
                    title,
                    null,
                    displayModel.largeStyle,
                    defaultColor,
                    highlightColor,
                    if (it.rawData().darkStyle) R.color.darkMain else R.color.lightMain,
                    it.rawData().separatorVisible,
                    separatorColor,
                    highlightColor,
                    false
            )
        }

        return menuCellModels.map {
            StaticTextCellViewModel(it)
        }
    }
}