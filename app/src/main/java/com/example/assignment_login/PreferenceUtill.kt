package com.example.assignment_login

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtill(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("pref_name", Context.MODE_PRIVATE)

    fun getString(id: String, password: String): String {
        return pref.getString(id, password).toString()
    }

    fun setString(id: String, password: String) {
        pref.edit().putString(id, password).apply()
    }
}