package com.ilustris.alicia.utils

import android.content.Context
import android.content.SharedPreferences
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
        getEditor().putLong(key, value)
    }


     fun getStringKey(key: String) = sharedPreferences.getString(key, null)
     fun getLongKey(key: String) = sharedPreferences.getLong(key, 0)

}

 const val USER_KEY = "user_prefs"