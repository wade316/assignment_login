package com.example.assignment_login

import android.app.Application

class MyApp: Application() {
    companion object {
        lateinit var pref: PreferenceUtill
    }

    override fun onCreate() {
        pref = PreferenceUtill(applicationContext)
        super.onCreate()

    }
}