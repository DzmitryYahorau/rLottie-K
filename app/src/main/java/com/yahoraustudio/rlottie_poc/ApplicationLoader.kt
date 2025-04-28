package com.yahoraustudio.rlottie_poc

import android.app.Application
import android.os.Handler

class ApplicationLoader: Application() {

    override fun onCreate() {
        super.onCreate()
        applicationLoaderInstance = this
        applicationHandler = Handler(mainLooper)
    }
    companion object {
        var applicationLoaderInstance: ApplicationLoader? = null
        var applicationHandler: Handler? = null
    }
}