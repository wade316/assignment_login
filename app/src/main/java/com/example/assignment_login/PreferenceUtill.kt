package com.example.assignment_login

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtill(context: Context) {
    private val pref: SharedPreferences =
        context.getSharedPreferences("pref_users", Context.MODE_PRIVATE)

    fun getUser(id: String): String {
        val get = pref.getString(id, id)
        return get.toString()
    }

    fun allClear() {
        pref.edit().clear().apply()
    }
    fun clearuser(id: String, password: String) {
        pref.edit().remove(id).apply()
        pref.edit().remove(password).apply()

    }

    fun containId(id: String): Boolean {
        return pref.contains(id)
    }

    fun addUser(id: String, password: String) {
        pref.edit()
            .putString(id, password)
            .apply()
    }
}