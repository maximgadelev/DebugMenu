package ru.kpfu.itis.debugmenu.feature.own_functionality.debuMenuVies

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import kotlinx.parcelize.Parcelize
import ru.kpfu.itis.debugmenu.R
import ru.kpfu.itis.debugmenu.feature.own_functionality.debugMenuView.DebugMenuView

@Parcelize
class ViewOne : DebugMenuView() {

    override var layout: Int = R.layout.view_button

    override fun provideAction(activity: Activity) {
         AlertDialog.Builder(activity)
            .setTitle("Delete entry")
            .setMessage("Are you sure you want to delete this entry?")
            .setNegativeButton(android.R.string.no, null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }
}