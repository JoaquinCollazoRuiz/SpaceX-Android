package com.joaquincollazoruiz.spacex.shared

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

/**
 * Custom test runner to replace [com.joaquincollazoruiz.spacex.SpaceXApplication]
 * with [AndroidTestApplication] to avoid running production app [Application] logic,
 * such as Koin dependency injection
 */
class CustomAndroidTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, AndroidTestApplication::class.java.name, context)
    }
}