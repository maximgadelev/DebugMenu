package ru.kpfu.itis.debugmenu.di

import ru.kpfu.itis.debugmenu.feature.debugmenu.provider.DebugMenuProvider
import ru.kpfu.itis.debugmenu.base.App
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenu
import toothpick.config.Module

class AppModule() : Module() {
    init {
        bind(DebugMenu::class.java)
            .toProvider(DebugMenuProvider::class.java)
            .providesSingleton()
    }
}