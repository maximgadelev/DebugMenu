package ru.kpfu.itis.debugmenu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.debugmenu.base.EnvironmentRepositoryImpl
import ru.kpfu.itis.debugmenu.databinding.FragmentMainBinding
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenu
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenuImpl
import ru.kpfu.itis.debugmenu.feature.debugmenu.OpenType
import ru.kpfu.itis.debugmenu.feature.network.provider.provideApi
import ru.kpfu.itis.debugmenu.feature.network.provider.provideChucker
import ru.kpfu.itis.debugmenu.feature.network.provider.provideJson
import ru.kpfu.itis.debugmenu.feature.network.provider.provideOkHttpClient
import ru.kpfu.itis.debugmenu.feature.network.provider.provideRetrofit
import ru.kpfu.itis.debugmenu.feature.own_functionality.debuMenuVies.ViewOne
import ru.kpfu.itis.debugmenu.feature.own_functionality.debuMenuVies.ViewTwo

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var debugMenu: DebugMenu

    private val binding: FragmentMainBinding by viewBinding(FragmentMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        debugMenu = DebugMenuImpl(requireContext(), BuildConfig.SERVER_URLS)
        binding.root.setOnClickListener {
            debugMenu.open(OpenType.DIALOG, arrayOf(ViewOne(), ViewTwo()))
        }
        val api = provideApi(
            provideRetrofit(
                okHttpClient = provideOkHttpClient(provideChucker(requireContext())),
                json = provideJson(),
                environmentRepository = EnvironmentRepositoryImpl(
                    requireContext(),
                    BuildConfig.SERVER_URLS
                )
            )
        )

        binding.btnFragment.setOnClickListener {
            debugMenu.open(OpenType.FRAGMENT, arrayOf(ViewOne(), ViewTwo()))
        }
        binding.btnDialog.setOnClickListener {
            debugMenu.open(OpenType.DIALOG, arrayOf(ViewOne(), ViewTwo()))
        }
        binding.btnBottomSheet.setOnClickListener {
            debugMenu.open(OpenType.BOTTOM_SHEET, arrayOf(ViewOne(), ViewTwo()))
        }
        binding.btnDo.setOnClickListener {
            lifecycleScope.launch {
                api.test()
            }
        }
    }
}