package com.appamedix.makula

import android.app.Application
import com.appamedix.makula.worker.datamodel.DataModelManager
import com.appamedix.makula.worker.preference.InternalSettings

class MakulaApp : Application() {

    override fun onCreate() {
        super.onCreate()
        InternalSettings.init(this)
        DataModelManager.init(this)
    }
}