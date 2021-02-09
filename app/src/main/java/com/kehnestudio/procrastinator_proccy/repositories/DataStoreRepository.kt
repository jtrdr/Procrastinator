package com.kehnestudio.procrastinator_proccy.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

const val PREFERENCE_NAME ="my_preference"

class DataStoreRepository(context: Context){

    private object PreferenceKeys{
        val last_sync = stringPreferencesKey("last_synchronisation")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    suspend fun saveToDataStore(lastSyncDate: String){
        dataStore.edit { preference ->
            preference[PreferenceKeys.last_sync] = lastSyncDate
        }
    }

    val readFromDataStore: Flow<String> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Timber.d("DataStore ${exception.message.toString()}")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val lastSyncDate = preference[PreferenceKeys.last_sync] ?: "none"
            lastSyncDate
        }


}