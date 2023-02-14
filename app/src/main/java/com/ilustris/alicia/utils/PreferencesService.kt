package com.ilustris.alicia.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.ilustris.alicia.BuildConfig
import javax.inject.Inject

class PreferencesService @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    private fun getEditor() = sharedPreferences.edit()

    fun updateStringKey(key: String, value: String) {
        getEditor().putString(key, value).commit()
    }

    fun updateLongKey(key: String, value: Long) {
        Log.i(javaClass.simpleName, "updating key $key with value $value")
        getEditor().putLong(key, value).commit()
    }

    fun updateBooleanKey(key: String, value: Boolean) {
        getEditor().putBoolean(key, value).commit()
    }

    fun getBooleanKey(key: String) = sharedPreferences.getBoolean(key, false)
    fun getStringKey(key: String) = sharedPreferences.getString(key, null)
    fun getLongKey(key: String) = sharedPreferences.getLong(key, 0)

}

const val USER_KEY = "user_prefs"
const val GOAL_KEY = "goal_prefs"
const val STATEMENT_KEY = "statement_prefs"