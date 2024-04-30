package ru.kpfu.itis.debugmenu.feature.debugmenu

import ru.kpfu.itis.debugmenu.feature.own_functionality.debugMenuView.DebugMenuView

interface DebugMenu {
    fun start()
    fun stop()
    fun open(
        type: OpenType = OpenType.FRAGMENT,
        debugMenuViews: Array<DebugMenuView> = emptyArray()
    )
}

enum class OpenType {
    DIALOG,
    BOTTOM_SHEET,
    FRAGMENT,
    DRAWER
}