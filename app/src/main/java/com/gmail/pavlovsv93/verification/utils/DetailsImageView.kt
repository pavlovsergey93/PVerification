package com.gmail.pavlovsv93.verification.utils

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class DetailsImageView
@JvmOverloads constructor(
	context: Context,
	attrSet: AttributeSet? = null,
	defaultStyle: Int = 0
) : AppCompatImageView(context, attrSet, defaultStyle) {
	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec)
	}
}