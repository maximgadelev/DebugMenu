package ru.kpfu.itis.debugmenu.feature.debugmenu.presentation

import io.reactivex.disposables.Disposable
import moxy.InjectViewState
import moxy.MvpPresenter
import ru.kpfu.itis.debugmenu.feature.debugmenu.data.repository.DebugOptionsRepository
import ru.kpfu.itis.debugmenu.feature.debugmenu.entity.EnvironmentsData
import javax.inject.Inject

@InjectViewState
class DebugMenuPresenter @Inject constructor(
    private val debugOptionsRepository: DebugOptionsRepository,
    private val environmentsData: EnvironmentsData
) : MvpPresenter<DebugMenuView>() {

    private var applyDisposable: Disposable? = null

    var isBackNavigationBlocked = false

    override fun onFirstViewAttach() {
        viewState.show(
            environmentsData.serverUrls,
            debugOptionsRepository.selectedUrl
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        applyDisposable?.dispose()
    }

    fun onApplyClicked(
        serverUrl: String,
    ) {
        with(debugOptionsRepository) {
            selectedUrl = serverUrl
        }
        viewState.showLoading(true)
        viewState.restartApp()
        viewState.showLoading(false)
    }
}