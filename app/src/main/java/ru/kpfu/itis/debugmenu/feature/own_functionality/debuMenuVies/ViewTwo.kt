package ru.kpfu.itis.debugmenu.feature.own_functionality.debuMenuVies

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import kotlinx.parcelize.Parcelize
import ru.kpfu.itis.debugmenu.R
import ru.kpfu.itis.debugmenu.feature.own_functionality.debugMenuView.DebugMenuView

@Parcelize
class ViewTwo : DebugMenuView() {

    override var layout: Int = R.layout.view_button_second

    override fun provideAction(activity: Activity) {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Важное сообщение!")
            .setMessage("Покормите кота!")
            .setPositiveButton("ОК, иду на кухню") { dialog, id ->
                dialog.cancel()
            }
        builder.create()
    }
}