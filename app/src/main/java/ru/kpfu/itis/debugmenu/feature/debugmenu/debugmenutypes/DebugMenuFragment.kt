package ru.kpfu.itis.debugmenu.feature.debugmenu.debugmenutypes

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.hbisoft.hbrecorder.HBRecorderListener
import com.jakewharton.processphoenix.ProcessPhoenix
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.kpfu.itis.crashreporter.crashreporter.src.main.java.com.balsikandar.crashreporter.ui.CrashReporterActivity
import ru.kpfu.itis.debugmenu.R
import ru.kpfu.itis.debugmenu.databinding.FragmentDebugMenuBinding
import ru.kpfu.itis.debugmenu.feature.debugmenu.DebugMenuImpl.Companion.DEBUG_MENU_SCOPE
import ru.kpfu.itis.debugmenu.feature.debugmenu.entity.EnvironmentsData
import ru.kpfu.itis.debugmenu.feature.debugmenu.presentation.DebugMenuPresenter
import ru.kpfu.itis.debugmenu.feature.debugmenu.presentation.DebugMenuView
import ru.kpfu.itis.debugmenu.feature.recorder.DebugRecorder
import toothpick.Toothpick
import java.io.File
import java.io.FileOutputStream
import java.util.Date
import javax.inject.Inject

class DebugMenuFragment : MvpAppCompatFragment(), DebugMenuView, HBRecorderListener {

    @Inject
    lateinit var environmentsData: EnvironmentsData

    @InjectPresenter
    lateinit var presenter: DebugMenuPresenter

    private var hasPermissions = false

    private var debugRecorder: DebugRecorder? = null

    @ProvidePresenter
    fun providePresenter(): DebugMenuPresenter =
        Toothpick
            .openScope(DEBUG_MENU_SCOPE)
            .getInstance(DebugMenuPresenter::class.java)

    private var _binding: FragmentDebugMenuBinding? = null

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DEBUG_MENU_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDebugMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        val views =
            arguments?.getParcelableArray(DebugMenuBottomDialogFragment.KEY_VIEWS) as Array<ru.kpfu.itis.debugmenu.feature.own_functionality.debugMenuView.DebugMenuView>
        views.forEach { menuView ->
            val newView =
                LayoutInflater.from(requireContext())
                    .inflate(menuView.layout, binding.linearLayout2, false)
            binding.linearLayout2.addView(newView)
            newView.setOnClickListener {
                menuView.provideAction(requireActivity())
            }
        }
        debugRecorder = DebugRecorder(requireContext().applicationContext, this)
        changeRecorder(debugRecorder)
        binding.btnStartRecording.setOnClickListener {
            checkPermission()
        }
        binding.btnStopRecording.setOnClickListener {
            debugRecorder?.stopScreenRecording()
        }
        binding.takePicture.setOnClickListener {
            takeScreenshot(requireActivity())
        }
        binding.btnOpenCrashes.setOnClickListener {
            val intent = Intent(
                requireContext(),
                CrashReporterActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            requireContext().startActivity(intent)
        }
    }

    private fun setupViews() {
        with(binding) {
            applyButton.setOnClickListener {
                val selectedUrl =
                    serverUrlRadioGroup.getSelectedButtonText() ?: return@setOnClickListener
                presenter.onApplyClicked(selectedUrl.toString())
            }
            toolbar.apply {
                setTitle(R.string.debug_menu)
                setNavigationIcon(R.drawable.ic_cross_toolbar)
                setNavigationOnClickListener { requireActivity().supportFragmentManager.popBackStack() }
            }
        }
    }

    override fun show(
        serverUrls: List<String>,
        selectedServerUrl: String
    ) {
        with(binding.serverUrlRadioGroup) {
            fillByStrings(serverUrls)
            showSelectedItem(selectedServerUrl)
        }
    }

    override fun showLoading(show: Boolean) {
        binding.loadingView.isVisible = show
    }

    override fun showError(error: Throwable) {
        Toast.makeText(
            requireContext(),
            "${error.javaClass.simpleName}: ${error.message ?: error.toString()}",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun closeScreen() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun restartApp() {
        ProcessPhoenix.triggerRebirth(requireContext().applicationContext)
    }

    private fun RadioGroup.getSelectedButtonText() =
        children
            .filterIsInstance(RadioButton::class.java)
            .firstOrNull { it.isChecked }
            ?.text

    private fun RadioGroup.fillByStrings(items: List<String>) {
        with(this) {
            removeAllViews()
            items.forEach { item ->
                addView(RadioButton(requireContext()).apply { text = item })
            }
        }
    }

    private fun RadioGroup.showSelectedItem(item: String) {
        this.children
            .filterIsInstance(RadioButton::class.java)
            .firstOrNull { it.text == item }
            ?.isChecked = true
    }


    private fun startRecordingScreen() {
        val mediaProjectionManager =
            requireActivity().getSystemService(AppCompatActivity.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
        val permissionIntent = mediaProjectionManager.createScreenCaptureIntent()
        startActivityForResult(permissionIntent, SCREEN_RECORD_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCREEN_RECORD_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                debugRecorder?.setOutputPath()
                debugRecorder?.startScreenRecording(data, resultCode);
            }
        }
    }

    private fun checkSelfPermission(permission: String, requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
            return false
        }
        return true
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(
                    Manifest.permission.POST_NOTIFICATIONS,
                    PERMISSION_REQ_POST_NOTIFICATIONS
                ) && checkSelfPermission(
                    Manifest.permission.RECORD_AUDIO,
                    PERMISSION_REQ_ID_RECORD_AUDIO
                )
            ) {
                hasPermissions = true
            }
        } else
            if (checkSelfPermission(
                    Manifest.permission.RECORD_AUDIO,
                    PERMISSION_REQ_ID_RECORD_AUDIO
                )
            ) {
                hasPermissions = true
            }
        if (hasPermissions) {
            if (debugRecorder!!.isBusyRecording) {
                debugRecorder!!.stopScreenRecording()
            } else {
                startRecordingScreen()
            }
        }
    }

    override fun HBRecorderOnStart() {
        if (isAdded) {
            Toast.makeText(requireContext(), "Ваш экран записывается", Toast.LENGTH_SHORT).show()
        }
    }

    override fun HBRecorderOnComplete() {
        if (isAdded) {
            Toast.makeText(requireContext(), "Запись завершена", Toast.LENGTH_SHORT).show()
        }
    }

    override fun HBRecorderOnError(errorCode: Int, reason: String?) {
        Toast.makeText(requireContext(), "Ошибка при записи", Toast.LENGTH_SHORT).show()
    }

    override fun HBRecorderOnPause() {

    }

    override fun HBRecorderOnResume() {

    }

    private fun takeScreenshot(activity: Activity): File {
        val now = Date()
        val date = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)
        val v1 = activity.window.decorView.rootView
        v1.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(v1.drawingCache)
        v1.isDrawingCacheEnabled = false

        val imageFile = File(
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "screenshot-$date.png"
        )

        val outputStream = FileOutputStream(imageFile)
        val quality = 100
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream)
        outputStream.flush()
        outputStream.close()
        return imageFile
    }

    companion object {
        const val SCREEN_RECORD_REQUEST_CODE = 777
        private const val PERMISSION_REQ_POST_NOTIFICATIONS = 33
        private const val PERMISSION_REQ_ID_RECORD_AUDIO = 22
        const val KEY_VIEWS = "KEY_VIEWS"
        private const val TAG = "BOTTOM_SHEET_TAG"

        fun newInstance(
            debugMenuView: Array<ru.kpfu.itis.debugmenu.feature.own_functionality.debugMenuView.DebugMenuView> = emptyArray()
        ): DebugMenuFragment {
            return DebugMenuFragment().apply {
                arguments = bundleOf(
                    KEY_VIEWS to debugMenuView
                )
            }
        }
    }


    private fun changeRecorder(recorder: DebugRecorder?) {
        recorder?.setNotificationTitle("Записываем ваш экран")
        recorder?.setNotificationButtonText("Сверните,чтобы остановить")
    }
}