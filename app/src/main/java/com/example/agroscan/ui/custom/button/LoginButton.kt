package com.example.agroscan.ui.custom.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.agroscan.R

class LoginButton : AppCompatButton {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: android.util.AttributeSet) : super(context, attrs)

    private var textColor: Int = 0
    private var enabledBackground: Drawable
    private var disabledBackground: Drawable

    init {
        textColor = ContextCompat.getColor(context, R.color.white)
        enabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_disable) as Drawable
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enabledBackground else disabledBackground
        setTextColor(textColor)
        textSize = 14f
        gravity = Gravity.CENTER
        text = if (isEnabled) "Login" else "Lengkapi data"
    }
}