package com.kemalatli.bubbleonboarding.background

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.widget.FrameLayout
import com.kemalatli.bubbleonboarding.shape.base.FocalShape


internal class BubbleBackgroundView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var eraserPaint:Paint? = null
    var internalBitmap:Bitmap? = null
    var internalCanvas:Canvas? = null

    internal var maskColor:Int = Color.parseColor("#dd335075")
    internal var focalShape: FocalShape? = null

    init {
        setWillNotDraw(false)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val internalWidth = measuredWidth
        val internalHeight = measuredHeight

        // Skip if dimensions are not defined
        if(internalHeight<=0 || internalWidth<=0) return

        // Create bitmap and canvas if needed
        if(internalBitmap== null || internalCanvas==null){
            if(internalBitmap!=null) internalBitmap?.recycle()
            val bitmap = Bitmap.createBitmap(internalWidth, internalHeight, Bitmap.Config.ARGB_8888)
            internalBitmap = bitmap
            internalCanvas = Canvas(bitmap)

        }

        // Clear canvas
        internalCanvas?.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        // Draw solid background
        internalCanvas?.drawColor(maskColor)

        // Prepare eraser Paint if needed
        if (eraserPaint == null) {
            eraserPaint = Paint().apply {
                color = -0x1
                xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                flags = ANTI_ALIAS_FLAG
            }
        }

        // Draw focal region
        focalShape?.draw(internalCanvas, eraserPaint)

        // Draw bitmap to scene
        internalBitmap?.let { canvas?.drawBitmap(it, 0f,0f, null) }

    }


}