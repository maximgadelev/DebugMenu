package ru.kpfu.itis.debugmenu.base

import android.content.Context
import ru.kpfu.itis.debugmenu.feature.debugmenu.data.storage.EnvironmentPreferencesStorage

class EnvironmentRepositoryImpl(
    context: Context,
    serverUrls: Array<String>,
) : EnvironmentRepository {

    private val preferencesStorage = EnvironmentPreferencesStorage.create(
        context,
        serverUrls,
    )

    override fun getServerBaseUrl(): String = preferencesStorage.selectedUrl
}