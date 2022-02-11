package ir.kindnesswall.utils

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import java.util.*


object LocaleHelper {

    private val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun onAttach(context: Context?): Context? {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttach(context: Context?, language: String): Context? {
        return setLocale(context, language)
    }

    fun getLanguage(context: Context): String? {
        return getPersistedData(context, Locale.getDefault().language)
    }

    fun setLocale(context: Context?, language: String?): Context? {
        persist(context, language)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else updateResourcesLegacy(context, language)

    }

    private fun getPersistedData(context: Context?, defaultLanguage: String): String? {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context?, language: String?) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
         preferences.edit { putString(SELECTED_LANGUAGE, language) }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context?, language: String?): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)

        if (context == null) return null

        val configuration = context.resources?.configuration ?: return null
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)

        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context?, language: String?): Context? {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val resources = context?.resources

        val configuration = resources?.configuration
        configuration?.locale = locale
        configuration?.setLayoutDirection(locale)

        resources?.updateConfiguration(configuration, resources.displayMetrics)

        return context
    }

    fun getLocaleStringResource(
        localeName: String,
        resourceId: Int,
        context: Context,
        value: String
    ): String {
        val result: String
        val config = Configuration(context.resources.configuration)
        config.setLocale(Locale(localeName))
        result = context.createConfigurationContext(config).getString(resourceId, value).toString()

        return result
    }
}