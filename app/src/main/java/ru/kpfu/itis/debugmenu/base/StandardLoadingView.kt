package ru.kpfu.itis.debugmenu.base

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.withStyledAttributes
import ru.kpfu.itis.debugmenu.R
import ru.kpfu.itis.debugmenu.databinding.ViewStandardLoadingBinding
import kotlin.properties.Delegates

class StandardLoadingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val viewBinding: ViewStandardLoadingBinding =
        ViewStandardLoadingBinding.inflate(LayoutInflater.from(context), this)

    private var text: CharSequence? by Delegates.observable(null) { _, _, newValue ->
        viewBinding.titleTextView.text = newValue
    }

    private var subtitle: CharSequence? by Delegates.observable(null) { _, _, newValue ->
        viewBinding.subTitleTextView.text = newValue
    }

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        isFocusableInTouchMode = true
        isFocusable = true
        isClickable = true

        context.withStyledAttributes(attrs, R.styleable.StandardLoadingView) {
            text = getText(R.styleable.StandardLoadingView_slv_loadingText)
            subtitle = getText(R.styleable.StandardLoadingView_slv_loading_subtitle)
            val backgroundResId =
                getResourceId(
                    R.styleable.StandardLoadingView_android_background,
                    R.color.background_loading
                )
            setBackgroundResource(backgroundResId)
        }
    }
}