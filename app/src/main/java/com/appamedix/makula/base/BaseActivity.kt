package com.appamedix.makula.base

import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.appamedix.makula.worker.locale.LocaleHelper

abstract class BaseActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase!!, "de"))
    }
}