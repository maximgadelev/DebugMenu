package ru.kpfu.itis.debugmenu.feature.own_functionality.debugMenuView

import android.app.Activity
import android.os.Parcelable

abstract class DebugMenuView : Parcelable {


    abstract var layout: Int

    abstract fun provideAction(activity: Activity)
}