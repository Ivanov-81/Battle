package com.example.battleofwits

import android.app.Application
import android.util.Log
import io.reactivex.plugins.RxJavaPlugins

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        RxJavaPlugins.setErrorHandler {
            Log.e(TAG_APP,"App RxError: ${it.message}")
        }
    }
}