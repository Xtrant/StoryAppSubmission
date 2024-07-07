package com.example.storyappsubmission.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(token: String, isLogin: Boolean) {
        dataStore.edit {preferences ->
            preferences[TOKEN] = token
            preferences[LOGIN_KEY] = isLogin
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences[TOKEN] = ""
            preferences[LOGIN_KEY] = false
        }
    }

    fun isLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[LOGIN_KEY] ?: false
        }
    }

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN] ?: ""
        }
    }

    companion object {
        private var INSTANCE: UserPreferences? = null

        private val TOKEN = stringPreferencesKey("token")
        private val LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}