package com.nxtru.bondscalc.data.storage

import android.content.Context

private const val SHARED_PREFS_NAME = "shared_prefs_name"

// https://youtu.be/vlQt5DBxaAI?list=PLeF3l86ZMVkLQbdRL6Ra4cr_cmPROj94y&t=1742
class SharedPrefsBondParamsStorage(context: Context) : BondParamsStorage {
    private val sharedPref = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveString(key: String, value: String): Boolean {
        sharedPref.edit().putString(key, value).apply()
        return true
    }

    override fun loadString(key: String, defaultValue: String): String =
        sharedPref.getString(key, defaultValue) ?: defaultValue

    override fun saveBoolean(key: String, value: Boolean): Boolean {
        sharedPref.edit().putBoolean(key, value).apply()
        return true
    }

    override fun loadBoolean(key: String, defaultValue: Boolean): Boolean =
        sharedPref.getBoolean(key, defaultValue)
}
