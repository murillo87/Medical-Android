package com.appamedix.makula.scenes.menu

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.scenes.amslertest.AmslerTestSceneActivity
import com.appamedix.makula.scenes.appointmentdetail.AppointmentDetailModel
import com.appamedix.makula.scenes.appointmentdetail.AppointmentDetailSceneActivity
import com.appamedix.makula.scenes.calendar.CalendarModel
import com.appamedix.makula.scenes.calendar.CalendarSceneActivity
import com.appamedix.makula.scenes.contact.ContactSceneActivity
import com.appamedix.makula.scenes.diagnosis.DiagnosisSceneActivity
import com.appamedix.makula.scenes.graph.EyeType
import com.appamedix.makula.scenes.graph.GraphModel
import com.appamedix.makula.scenes.graph.GraphSceneActivity
import com.appamedix.makula.scenes.info.InfoModel
import com.appamedix.makula.scenes.info.InfoSceneActivity
import com.appamedix.makula.scenes.medicament.MedicamentSceneActivity
import com.appamedix.makula.scenes.menu.table.MenuTableAdapter
import com.appamedix.makula.scenes.menu.table.MenuTableDataSource
import com.appamedix.makula.scenes.newappointment.NewAppointmentSceneActivity
import com.appamedix.makula.scenes.readingtest.ReadingTestSceneActivity
import com.appamedix.makula.scenes.reminder.ReminderSceneActivity
import com.appamedix.makula.scenes.search.SearchSceneActivity
import com.appamedix.makula.scenes.visusnhd.VisusNhdInputModel
import com.appamedix.makula.scenes.visusnhd.VisusNhdInputSceneActivity
import com.appamedix.makula.types.*
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.views.textcell.StaticTextCellView
import com.appamedix.makula.views.textcell.StaticTextCellViewModel
import com.appamedix.makula.worker.datamodel.appointment.getLastAppointments
import com.appamedix.makula.worker.speech.SpeechSynthesizer
import com.appamedix.makula.worker.speech.SpeechSynthesizerListener
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_menu_scene.*
import java.util.*

class MenuSceneActivity : BaseActivity(), NavigationViewListener,
        CellItemListener, SpeechSynthesizerListener {

    companion object {
        const val MENU_MODEL = "menu_model"
    }

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: MenuTableAdapter? = null

    private var isLandscape: Boolean = false

    private var synthesizer: SpeechSynthesizer? = null
    private lateinit var displayModel: MenuDisplayModel

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_scene)

        realm = Realm.getDefaultInstance()

        // Initialize synthesizer
        synthesizer = SpeechSynthesizer(this)

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        requestDisplayData()

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(displayModel.sceneId.titleString()),
                R.color.white,
                false,
                true,
                displayModel.sceneId != SceneId.HomeMenu,
                ImageButtonType.Back,
                true,
                ImageButtonType.Speaker,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.menu_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
    }

    override fun onStart() {
        super.onStart()

        // Apply data source and adapter to the table view.
        val tableDataSource: MenuTableDataSource = ViewModelProviders.of(this).get(MenuTableDataSource::class.java)
        tableDataSource.realm = realm
        tableDataSource.getMenuCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableAdapter = MenuTableAdapter(this@MenuSceneActivity, cellViewModels!!, this)
            recyclerView!!.layoutManager = LinearLayoutManager(this@MenuSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })

        // Set up speech data.
        tableDataSource.getMenuCellSpeechData(displayModel).observe(this, Observer { speechData ->
            synthesizer?.setSpeechData(speechData!!)
        })
    }

    override fun onStop() {
        super.onStop()

        // Scroll to top.
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)

        // Stop synthesizer if speaking.
        synthesizer?.let { synthesizer ->
            if (synthesizer.isSpeaking()) {
                synthesizer.stopSpeaking()
            }
        }
    }

    override fun onDestroy() {
        synthesizer?.destroy()
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (displayModel.sceneId == SceneId.HomeMenu) {
            // Pressing the hardware back button while on the home menu scene should terminate the app.
            finishAffinity()
            System.exit(0)
        } else {
            super.onBackPressed()
            overridePendingTransition(R.anim.right_in, R.anim.right_out)
        }
    }

    private fun requestDisplayData() {
        var sceneId: SceneId = SceneId.HomeMenu
        intent.extras?.let {
            val menuModel = it.getParcelable<MenuModel>(MENU_MODEL)
            sceneId = menuModel?.sceneId ?: SceneId.HomeMenu
        }

        displayModel = MenuDisplayModel(
                this,
                isLandscape,
                sceneId
        )
    }

    /* Cell click listener */

    override fun onItemClicked(model: BaseCellViewModel, type: ViewCellType) {
        if (type == ViewCellType.StaticTextCell) {
            val cellModel = model as StaticTextCellViewModel
            menuItemClicked(cellModel)
        }
    }

    private fun menuItemClicked(menuItem: StaticTextCellViewModel) {
        when (menuItem.toSceneId){
            SceneId.DoctorVisitMenu, SceneId.SelfTestMenu,
            SceneId.KnowledgeMenu, SceneId.SettingsMenu,
            SceneId.IllnessMenu, SceneId.ExaminationMenu,
            SceneId.TherapyMenu, SceneId.ActivitiesMenu,
            SceneId.AidMenu, SceneId.SupportMenu -> {
                routeToMenu(menuItem.toSceneId)
            }
            SceneId.NewAppointment -> routeToNewAppointment()
            SceneId.Contact -> routeToContact()
            SceneId.Calendar -> routeToCalendar()
            SceneId.AppointmentDetail -> routeToAppointmentDetail()
            SceneId.Diagnosis -> routeToDiagnosis()
            SceneId.Medicament -> routeToMedicament()
            SceneId.VisusInput -> routeToVisusNhdInput(VisusNhdType.Visus)
            SceneId.NhdInput -> routeToVisusNhdInput(VisusNhdType.Nhd)
            SceneId.Graph -> routeToGraph()
            SceneId.AmslerTest -> routeToAmslerTest()
            SceneId.ReadingTest -> routeToReadingTest()
            SceneId.Info -> routeToInfo(menuItem.cellIdentifier.infoType())
            SceneId.Search -> routeToSearch()
            SceneId.Reminder -> routeToReminder()
            else -> return
        }
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    /* Routing */

    private fun routeToMenu(sceneId: SceneId) {
        val menuModel = MenuModel(sceneId)
        val intent = Intent(this, MenuSceneActivity::class.java)
        intent.putExtra(MENU_MODEL, menuModel)
        startActivity(intent)
    }

    private fun routeToNewAppointment() {
        val intent = Intent(this, NewAppointmentSceneActivity::class.java)
        startActivity(intent)
    }

    private fun routeToContact() {
        val intent = Intent(this, ContactSceneActivity::class.java)
        startActivity(intent)
    }

    private fun routeToCalendar() {
        val calendarModel = CalendarModel(null, false)
        val intent = Intent(this, CalendarSceneActivity::class.java)
        intent.putExtra(CalendarSceneActivity.CALENDAR_MODEL, calendarModel)
        startActivity(intent)
    }

    private fun routeToAppointmentDetail() {
        // Find the last appointments.
        val today = Date()
        val lastAppointments = realm.getLastAppointments(today)

        // Route to appointment detail scene.
        val focusDate = lastAppointments.first().getAppointmentDate()
        val detailModel = AppointmentDetailModel(focusDate)
        val intent = Intent(this, AppointmentDetailSceneActivity::class.java)
        intent.putExtra(AppointmentDetailSceneActivity.DETAIL_MODEL, detailModel)
        startActivity(intent)
    }

    private fun routeToDiagnosis() {
        val intent = Intent(this, DiagnosisSceneActivity::class.java)
        startActivity(intent)
    }

    private fun routeToMedicament() {
        val intent = Intent(this, MedicamentSceneActivity::class.java)
        startActivity(intent)
    }

    private fun routeToVisusNhdInput(sceneType: VisusNhdType) {
        val visusNhdModel = VisusNhdInputModel(sceneType)
        val intent = Intent(this, VisusNhdInputSceneActivity::class.java)
        intent.putExtra(VisusNhdInputSceneActivity.VISUS_NHD_MODEL, visusNhdModel)
        startActivity(intent)
    }

    private fun routeToGraph() {
        val graphModel = GraphModel(EyeType.Left, false)
        val intent = Intent(this, GraphSceneActivity::class.java)
        intent.putExtra(GraphSceneActivity.GRAPH_MODEL, graphModel)
        startActivity(intent)
    }

    private fun routeToAmslerTest() {
        val intent = Intent(this, AmslerTestSceneActivity::class.java)
        startActivity(intent)
    }

    private fun routeToReadingTest() {
        val intent = Intent(this, ReadingTestSceneActivity::class.java)
        startActivity(intent)
    }

    private fun routeToInfo(sceneType: InfoType) {
        val infoModel = InfoModel(sceneType)
        val intent = Intent(this, InfoSceneActivity::class.java)
        intent.putExtra(InfoSceneActivity.INFO_MODEL, infoModel)
        startActivity(intent)
    }

    private fun routeToSearch() {
        val intent = Intent(this, SearchSceneActivity::class.java)
        startActivity(intent)
    }

    private fun routeToReminder() {
        val intent = Intent(this, ReminderSceneActivity::class.java)
        startActivity(intent)
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        synthesizer?.let { synthesizer ->
            if (synthesizer.isSpeaking()) {
                synthesizer.stopSpeaking()
            } else {
                synthesizer.startSpeaking()
            }
        }
    }

    /* SpeechSynthesizer listener */

    override fun speechStarted(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val position = data.position ?: 0

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(position, 0)

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            (holder as StaticTextCellView).applyHighlightColor()
        }
    }

    override fun speechEnded(data: SpeechSynthesizer.SpeechData) {
        val recyclerView = this.recyclerView ?: return
        val position = data.position ?: 0

        val viewCell = recyclerView.findViewHolderForAdapterPosition(position)
        viewCell?.let { holder ->
            (holder as StaticTextCellView).applyDefaultColor()
        }
    }

    override fun speechFinished() {
        val recyclerView = this.recyclerView ?: return
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(0, 0)
    }
}
