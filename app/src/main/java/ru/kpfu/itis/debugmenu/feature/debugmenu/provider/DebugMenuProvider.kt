package ru.kpfu.itis.debugmenu.feature.debugmenu.provider

import android.app.Application
import ru.kpfu.itis.debugmenu.BuildConfig
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenu
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenuImpl
import javax.inject.Inject
import javax.inject.Provider

class DebugMenuProvider @Inject constructor(
    private val context: Application,
) : Provider<DebugMenu> {

    override fun get(): DebugMenu {
        return DebugMenuImpl(
            context,
            BuildConfig.SERVER_URLS
        )
    }
}