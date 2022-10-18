package com.nxtru.bondscalc.data.storage

// https://youtu.be/vlQt5DBxaAI?list=PLeF3l86ZMVkLQbdRL6Ra4cr_cmPROj94y&t=1742
interface BondParamsStorage {
    fun saveString(key: String, value: String): Boolean
    fun loadString(key: String, defaultValue: String = ""): String
    fun saveBoolean(key: String, value: Boolean): Boolean
    fun loadBoolean(key: String, defaultValue: Boolean = false): Boolean
}
