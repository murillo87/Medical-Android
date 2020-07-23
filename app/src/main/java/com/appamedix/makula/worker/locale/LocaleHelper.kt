package com.appamedix.makula.worker.locale

import android.content.Context
import android.content.res.Configuration
import com.appamedix.makula.worker.preference.InternalSettings
import java.util.*

/**
 * This class is used to change your application locale and persist this change for the next time
 * that your app is going to be used.
 *
 * You can also change the locale of your application on the fly by using the `setLocale` method.
 */
object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        InternalSettings.selectedLanguage = language
        return updateResources(context, language)
    }

    private fun updateResources(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.setLocale(locale)

        return context.createConfigurationContext(configuration)
    }
}