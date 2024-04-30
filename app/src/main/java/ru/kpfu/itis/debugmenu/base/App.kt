package ru.kpfu.itis.debugmenu.base

import android.app.ActivityManager
import android.app.Application
import android.os.Process
import ru.kpfu.itis.crashreporter.crashreporter.src.main.java.com.balsikandar.crashreporter.CrashReporter
import ru.kpfu.itis.debugmenu.BuildConfig
import ru.kpfu.itis.debugmenu.di.AppModule
import ru.kpfu.itis.debugmenu.di.DI
import toothpick.Toothpick
import toothpick.configuration.Configuration

class App : Application() {


    override fun onCreate() {
        super.onCreate()
        initAppScope()
        CrashReporter.initialize(this)
    }

    private fun initAppScope() {
        if (isMainProcess()) {
            val configuration = if (BuildConfig.DEBUG) {
                Configuration.forDevelopment()
            } else {
                Configuration.forProduction()
            }
            Toothpick.setConfiguration(configuration)
            Toothpick
                .openScope(DI.APP_SCOPE)
                .installModules(AppModule())

            Toothpick.inject(this, Toothpick.openScope(DI.APP_SCOPE))

        }
    }

    private fun isMainProcess(): Boolean = packageName == getCurrentProcessName()

    private fun getCurrentProcessName(): String? {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val infos = activityManager.runningAppProcesses
        for (info in infos) {
            if (info.pid == Process.myPid()) {
                return info.processName
            }
        }
        return null
    }
}