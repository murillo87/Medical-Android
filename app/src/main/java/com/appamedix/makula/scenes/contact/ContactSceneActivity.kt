package com.appamedix.makula.scenes.contact

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.appamedix.makula.R
import com.appamedix.makula.base.BaseActivity
import com.appamedix.makula.base.BaseCellViewModel
import com.appamedix.makula.global.listener.CellItemListener
import com.appamedix.makula.scenes.contact.table.ContactTableAdapter
import com.appamedix.makula.scenes.contact.table.ContactTableDataSource
import com.appamedix.makula.scenes.contact.table.maincell.ContactCellListener
import com.appamedix.makula.scenes.contactdetail.ContactDetailModel
import com.appamedix.makula.scenes.contactdetail.ContactDetailSceneActivity
import com.appamedix.makula.types.ImageButtonType
import com.appamedix.makula.types.ViewCellType
import com.appamedix.makula.views.navigation.NavigationCellModel
import com.appamedix.makula.views.navigation.NavigationCellViewModel
import com.appamedix.makula.views.navigation.NavigationViewListener
import com.appamedix.makula.worker.datamodel.contact.*
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_contact_scene.*

class ContactSceneActivity : BaseActivity(), NavigationViewListener,
        CellItemListener, ContactCellListener {

    private var recyclerView: RecyclerView? = null
    private var tableAdapter: ContactTableAdapter? = null

    private var isLandscape: Boolean = false
    private lateinit var displayModel: ContactDisplayModel
    private lateinit var contactObjects: RealmResults<ContactObject>
    private lateinit var tableCellViewModels: ArrayList<BaseCellViewModel>

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_scene)

        realm = Realm.getDefaultInstance()

        // Check the device orientation.
        val orientation = resources.configuration.orientation
        isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

        // Setup navigation
        val navigationCellModel = NavigationCellModel(
                getString(R.string.contactTitle),
                R.color.white,
                false,
                true,
                true,
                ImageButtonType.Back,
                true,
                ImageButtonType.Add,
                this)
        val navigationCellViewModel = NavigationCellViewModel(navigationCellModel)
        navigation.setupView(navigationCellViewModel)

        if (isLandscape) {
            navigation.layoutParams.height = 0
        }

        // Setup table view.
        recyclerView = findViewById(R.id.contact_table)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
    }

    override fun onStart() {
        super.onStart()

        // Sets up display model.
        requestDisplayData()
        // Show table.
        presentTable()
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_in, R.anim.right_out)
    }

    private fun requestDisplayData() {
        contactObjects = realm.getContactObjects()

        // Prepare display model.
        displayModel = ContactDisplayModel(this, isLandscape, contactObjects)
    }

    /**
     * Apply data source and adapter to the table view.
     */
    private fun presentTable() {
        val dataSource: ContactTableDataSource = ViewModelProviders.of(this).get(ContactTableDataSource::class.java)
        dataSource.getMainCellData(displayModel).observe(this, Observer { cellViewModels ->
            tableCellViewModels = cellViewModels!!
            tableAdapter = ContactTableAdapter(this@ContactSceneActivity, cellViewModels, this)
            recyclerView!!.layoutManager = LinearLayoutManager(this@ContactSceneActivity)
            recyclerView!!.adapter = tableAdapter
        })
    }

    /**
     * Routes to the contact detail scene.
     *
     * @param withModel: The contact detail model to transfer.
     */

    private fun routeToContactDetail(withModel: ContactDetailModel) {
        val intent = Intent(this, ContactDetailSceneActivity::class.java)
        intent.putExtra(ContactDetailSceneActivity.CONTACT_DETAIL_MODEL, withModel)
        startActivity(intent)
        overridePendingTransition(R.anim.left_in, R.anim.left_out)
    }

    /**
     * Shows database error
     */
    private fun databaseWriteError() {
        Toast.makeText(this, R.string.databaseWriteErrorMessage, Toast.LENGTH_SHORT).show()
    }

    /* Cell click listener */

    override fun onItemClicked(model: BaseCellViewModel, type: ViewCellType) {
        if (type == ViewCellType.ContactCell) {
            val index = tableCellViewModels.indexOf(model)
            val contactObject = if (isLandscape) contactObjects[index - 1] else contactObjects[index]
            contactObject?.let {
                val contactDetailModel = ContactDetailModel(it.creationDate)
                routeToContactDetail(contactDetailModel)
            }
        }
    }

    /* Contact cell listener */

    override fun deleteButtonClicked(position: Int) {
        val index = if (isLandscape) position - 1 else position

        val contactObject = contactObjects[index]
        contactObject?.let {
            if (!realm.deleteContactObject(it)) {
                databaseWriteError()
            }
        }
    }

    /* Navigation view listener */

    override fun leftButtonClicked() {
        onBackPressed()
    }

    override fun rightButtonClicked() {
        val contactObject = realm.createEmptyContactObject()

        if (contactObject == null) {
            databaseWriteError()
            return
        } else {
            val contactDetailModel = ContactDetailModel(contactObject.creationDate)
            routeToContactDetail(contactDetailModel)
        }
    }
}
