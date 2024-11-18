package com.example.newsapp.screen

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


val Context.dataStore by preferencesDataStore(name = "settings")

suspend fun saveSelectedSettingOption(context: Context, option: String, settingDataType: String) {

    val key = stringPreferencesKey(settingDataType)



    context.dataStore.edit { preferences ->
        preferences[key] = option
    }

}

fun getSettingsDataFlow(context: Context, settingDataType: String): Flow<String> {

    val dataKey = stringPreferencesKey(settingDataType)
    return context.dataStore.data
        .map { preferences ->
            when (settingDataType) {

                "Country" -> preferences[dataKey] ?: "All Country"

                "Theme" -> preferences[dataKey] ?: "System"

                "Language" -> preferences[dataKey] ?: "English"

                "Aura" -> preferences[dataKey] ?: "Blue"

                "Sort searched news" -> preferences[dataKey] ?: "Relevancy"

                "Sort headlines" -> preferences[dataKey] ?: "Popularity"

                else -> {
                    preferences[dataKey] ?: ""
                }
            }

        }
}
