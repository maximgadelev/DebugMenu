package ru.kpfu.itis.debugmenu.feature.debugmenu.data.repository

import ru.kpfu.itis.debugmenu.feature.debugmenu.data.storage.EnvironmentPreferencesStorage
import javax.inject.Inject

class DebugOptionsRepository @Inject constructor(
    private val preferencesStorage: EnvironmentPreferencesStorage
) {

    var selectedUrl: String
        get() = preferencesStorage.selectedUrl
        set(value) {
            preferencesStorage.selectedUrl = value
        }
}