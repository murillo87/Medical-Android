package com.appamedix.makula.scenes.graph

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
import com.appamedix.makula.constants.Const
import com.appamedix.makula.scenes.graph.table.GraphTableAdapter
import com.appamedix.makula.scenes.graph.table.GraphTableDataSource
import com.appamedix.makula.scenes.menu.MenuModel
import com.appamedix.makula.scenes.menu.MenuSceneActivity
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.SceneId
import com.appamedix.makula.utils.DateUtils
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.views.splitcell.SplitCellListener
import com.appamedix.makula.worker.datamodel.appointment.getLastTreatmentObjects
import com.appamedix.makula.worker.datamodel.histogram.getNhdObjects
import com.appamedix.makula.worker.datamodel.histogram.getVisusObjects
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_graph_scene.*
import java.util.*
import kotlin.collections.ArrayList

class GraphSceneActivity : BaseActivity(), NavigationViewListener, SplitCellListener {

    companion object {
        const val GRAPH_MODEL = "graph_model"
    }

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: GraphTableAdapter? = null

    private var isLandscape: Boolean = false
    private var isModifyNavigationStackSet: Boolean = false

    private var eyeType: EyeType = EyeType.Left
    private lateinit var displayModel: GraphDisplayModel

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Get data from intent.
        intent.extras?.let {
            val graphModel = it.getParcelable<GraphModel>(GRAPH_MODEL)
            eyeType = graphModel?.eyeType ?: EyeType.Left
            isModifyNavigationStackSet = graphModel?.modifyNavigationStack ?: false
        }

        // Sets up display model.
        requestDisplayData()

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(R.string.graphSceneTitle),
                R.color.white,
                false,
                true,
                true,
                ImageButtonType.Back,
                false,
                ImageButtonType.Speaker,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.graph_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        presentTable()
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (isModifyNavigationStackSet) {
            val menuModel = MenuModel(SceneId.DoctorVisitMenu)
            val intent = Intent(this, MenuSceneActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(MenuSceneActivity.MENU_MODEL, menuModel)
            startActivity(intent)
        } else {
            super.onBackPressed()
        }
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun requestDisplayData() {
        // Graph's start and end date.
        val today = Date()
        val endDate = DateUtils.increaseMonth(DateUtils.startOfMonth(today), 1)
        val startDate = DateUtils.increaseMonth(endDate, -Const.Graph.xAxisSteps.toInt())

        // Get the realm objects.
        val nhdObjects = realm.getNhdObjects(startDate, endDate)
        val visusObjects = realm.getVisusObjects(startDate, endDate)
        val lastTreatment = realm.getLastTreatmentObjects(today)
        val ivomDates = ArrayList(lastTreatment.map { it.getAppointmentDate() })

        // Prepare display model.
        displayModel = GraphDisplayModel(
                this,
                isLandscape,
                eyeType,
                ivomDates,
                if (visusObjects == null) arrayListOf() else ArrayList(visusObjects),
                if (nhdObjects == null) arrayListOf() else ArrayList(nhdObjects)
        )
    }

    /**
     * Apply data source and adapter to the table view.
     */
    private fun presentTable() {
        val dataSource: GraphTableDataSource = ViewModelProviders.of(this).get(GraphTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableAdapter = GraphTableAdapter(this@GraphSceneActivity, cellViewModels!!)
            recyclerView!!.layoutManager = LinearLayoutManager(this@GraphSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {

    }

    /* Split cell listener */

    override fun leftButtonPressed() {
        if (eyeType == EyeType.Left) return

        eyeType = EyeType.Left
        requestDisplayData()
        presentTable()
    }

    override fun rightButtonPressed() {
        if (eyeType == EyeType.Right) return

        eyeType = EyeType.Right
        requestDisplayData()
        presentTable()
    }
}
