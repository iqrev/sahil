package com.dicoding.picodiploma.loginwithanimation.view.costumeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R

class MeineButton: AppCompatButton {
    private lateinit var enableBackground: Drawable
    private lateinit var disabledBackground: Drawable
    private var txtColor: Int = 0

    constructor(context: Context): super(context){
        init()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int): super(context, attrs, defStyleAttr){
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isEnabled) enableBackground else disabledBackground

        setTextColor(txtColor)
        textSize = 12f
        gravity = Gravity.CENTER
    }

    private fun init(){
        txtColor = ContextCompat.getColor(context, R.color.white)
        enableBackground = ContextCompat.getDrawable(context, R.drawable.bg_button) as Drawable
        disabledBackground = ContextCompat.getDrawable(context, R.drawable.bg_button_dissable) as Drawable
    }
}