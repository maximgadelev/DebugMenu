package ru.kpfu.itis.debugmenu.feature.debugmenu

import android.app.Service
import android.content.Context
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import com.squareup.seismic.ShakeDetector
import ru.kpfu.itis.debugmenu.R
import ru.kpfu.itis.debugmenu.di.DI
import ru.kpfu.itis.debugmenu.feature.debugmenu.data.repository.DebugOptionsRepository
import ru.kpfu.itis.debugmenu.feature.debugmenu.data.storage.EnvironmentPreferencesStorage
import ru.kpfu.itis.debugmenu.feature.debugmenu.debugmenutypes.DebugMenuBottomDialogFragment
import ru.kpfu.itis.debugmenu.feature.debugmenu.debugmenutypes.DebugMenuDialogFragment
import ru.kpfu.itis.debugmenu.feature.debugmenu.debugmenutypes.DebugMenuFragment
import ru.kpfu.itis.debugmenu.feature.debugmenu.entity.EnvironmentsData
import ru.kpfu.itis.debugmenu.feature.own_functionality.debugMenuView.DebugMenuView
import toothpick.Toothpick
import toothpick.config.Module

class DebugMenuImpl(
    private val context: Context,
    serverUrls: Array<String>
) : DebugMenu {

    init {
        Toothpick.openScopes(DI.APP_SCOPE, DEBUG_MENU_SCOPE).apply {
            installModules(
                Module().apply {
                    bind(EnvironmentsData::class.java).toInstance(
                        EnvironmentsData(
                            serverUrls.toList()
                        )
                    )
                    bind(EnvironmentPreferencesStorage::class.java).toInstance(
                        EnvironmentPreferencesStorage.create(
                            context,
                            serverUrls
                        )
                    )
                    bind(DebugOptionsRepository::class.java).singleton()
                }
            )
        }
    }

    private var shakeDetector: ShakeDetector? = null

    override fun start() {
        val sensorManager = context.getSystemService(Service.SENSOR_SERVICE) as SensorManager
        shakeDetector = ShakeDetector { open() }
        shakeDetector?.start(sensorManager, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun stop() {
        shakeDetector?.stop()
    }

    override fun open(type: OpenType, debugMenusView: Array<DebugMenuView>) {
        when (type) {
            OpenType.DIALOG -> {
                DebugMenuDialogFragment.newInstance(
                    (context as AppCompatActivity).supportFragmentManager,
                    debugMenusView
                )
            }

            OpenType.BOTTOM_SHEET -> {
                DebugMenuBottomDialogFragment.newInstance(
                    (context as AppCompatActivity).supportFragmentManager,
                    debugMenusView
                )
            }

            OpenType.FRAGMENT -> {
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = DebugMenuFragment.newInstance(
                    debugMenusView
                )
                fragmentTransaction.replace(
                    R.id.fragment_container,
                    fragment
                )
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }

            else -> {

            }
        }
    }

    companion object {
        const val DEBUG_MENU_SCOPE = "DEBUG_MENU_SCOPE"
    }
}