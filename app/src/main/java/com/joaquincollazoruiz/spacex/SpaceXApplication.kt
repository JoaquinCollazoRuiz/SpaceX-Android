package com.joaquincollazoruiz.spacex

import android.app.Application
import com.joaquincollazoruiz.spacex.di.KoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("UNUSED")
class SpaceXApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDI()
    }

    private fun setupDI() {
        startKoin {
            androidContext(this@SpaceXApplication)
            modules(KoinModules.all())
        }
    }
}