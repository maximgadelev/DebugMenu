package ru.kpfu.itis.debugmenu.feature.debugmenu.presentation

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface DebugMenuView : MvpView {

    fun show(
        serverUrls: List<String>,
        selectedServerUrl: String
    )

    fun showLoading(show: Boolean)

    fun showError(error: Throwable)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun closeScreen()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun restartApp()
}