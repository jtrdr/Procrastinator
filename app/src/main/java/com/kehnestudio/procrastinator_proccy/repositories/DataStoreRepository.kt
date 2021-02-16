package com.kehnestudio.procrastinator_proccy.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository.PreferenceKeys.checkBox1
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository.PreferenceKeys.checkBox2
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository.PreferenceKeys.checkBox3
import com.kehnestudio.procrastinator_proccy.repositories.DataStoreRepository.PreferenceKeys.checkBox4
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

const val PREFERENCE_NAME = "my_preference"

class DataStoreRepository(context: Context) {

    private object PreferenceKeys {
        val last_sync = stringPreferencesKey("last_synchronisation")
        val checkBox1 = booleanPreferencesKey("isChecked1")
        val checkBox2 = booleanPreferencesKey("isChecked2")
        val checkBox3 = booleanPreferencesKey("isChecked3")
        val checkBox4 = booleanPreferencesKey("isChecked4")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    suspend fun saveSyncDateToDataStore(lastSyncDate: String) {
        dataStore.edit { preference ->
            preference[PreferenceKeys.last_sync] = lastSyncDate
        }
    }

    suspend fun saveCheckBoxStates(
        isChecked1: Boolean,
        isChecked2: Boolean,
        isChecked3: Boolean,
        isChecked4: Boolean
    ) {
        dataStore.edit {
            it[checkBox1] = isChecked1
            it[checkBox2] = isChecked2
            it[checkBox3] = isChecked3
            it[checkBox4] = isChecked4
        }
    }

    suspend fun clearCheckBoxStates() {
        dataStore.edit {
            it[checkBox1] = false
            it[checkBox2] = false
            it[checkBox3] = false
            it[checkBox4] = false
        }
    }

    val readFromDataStore: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
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

    val readCheckBoxStates: Flow<MutableList<Boolean>> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d("DataStore ${exception.message.toString()}")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map {
            val states = mutableListOf(
                it[checkBox1] ?: false,
                it[checkBox2] ?: false,
                it[checkBox3] ?: false,
                it[checkBox4] ?: false
            )
            states
        }
}