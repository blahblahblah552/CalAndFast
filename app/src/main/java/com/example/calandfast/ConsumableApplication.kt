package com.example.calandfast


import android.app.Application
import com.example.calandfast.database.AppContainer
import com.example.calandfast.database.AppDataContainer

class ConsumableApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}