package ru.kpfu.itis.debugmenu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.debugmenu.base.EnvironmentRepositoryImpl
import ru.kpfu.itis.debugmenu.databinding.ActivityMainBinding
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenu
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenuImpl
import ru.kpfu.itis.debugmenu.feature.debugmenu.OpenType
import ru.kpfu.itis.debugmenu.feature.debugmenu.debugmenutypes.DebugMenuFragment
import ru.kpfu.itis.debugmenu.feature.network.provider.provideApi
import ru.kpfu.itis.debugmenu.feature.network.provider.provideChucker
import ru.kpfu.itis.debugmenu.feature.network.provider.provideJson
import ru.kpfu.itis.debugmenu.feature.network.provider.provideOkHttpClient
import ru.kpfu.itis.debugmenu.feature.network.provider.provideRetrofit
import ru.kpfu.itis.debugmenu.feature.own_functionality.debuMenuVies.ViewOne
import ru.kpfu.itis.debugmenu.feature.own_functionality.debuMenuVies.ViewTwo


class MainActivity : AppCompatActivity() {

    private lateinit var debugMenu: DebugMenu

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = MainFragment()
        fragmentTransaction.replace(
            R.id.fragment_container,
            fragment
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}