package com.kemalatli.bubbleonboarding.background

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.core.view.doOnLayout
import com.kemalatli.bubbleonboarding.content.bubble.ArrowLocation
import com.kemalatli.bubbleonboarding.content.bubble.BubbleDrawable
import com.kemalatli.bubbleonboarding.content.bubble.BubbleType
import com.kemalatli.bubbleonboarding.focus.base.FocalShape


internal class BubbleBackgroundView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    var eraserPaint:Paint? = null
    var internalBitmap:Bitmap? = null
    var internalCanvas:Canvas? = null

    internal var maskColor:Int = Color.parseColor("#dd335075")
    internal var focalShape: FocalShape? = null

    init {
        setWillNotDraw(false)
    }

    fun addBubble(bubble: View?, builder: BubbleDrawable.Builder) {

        // Bubble check
        if(bubble==null) return
        // Shape check
        val shape = focalShape ?: return
        // Screen width
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels;
        // Add bubble to measure
        bubble.measure(0,0)
        // Layout margins
        var arrowCenter = true
        var left = 0
        var top = 0
        var bubbleWidth = bubble.measuredWidth.toFloat()
        var bubbleHeight = bubble.measuredHeight.toFloat()
        when(builder.arrowLocation){
            is ArrowLocation.Bottom -> {
                left = (shape.viewX - (bubble.measuredWidth-shape.width)/2).toInt()
                top = (shape.viewY - bubble.measuredHeight - builder.bubbleMargin-builder.arrowHeight).toInt()
                // Check right
                if(left + bubble.measuredWidth > screenWidth){
                    left = screenWidth - bubble.measuredWidth
                    builder.arrowPosition(shape.viewX+shape.width/2-left)
                    arrowCenter = false
                }
                // Check left
                if(left<0){
                    left = 0
                    builder.arrowPosition(shape.viewX+shape.width/2-left)
                    arrowCenter = false
                }
                bubbleHeight += builder.arrowHeight
            }
            is ArrowLocation.Top -> {
                left = (shape.viewX - (bubble.measuredWidth-shape.width)/2).toInt()
                top = (shape.viewY + shape.heigth + builder.bubbleMargin+builder.arrowHeight).toInt()
                // Check right
                if(left + bubble.measuredWidth > screenWidth){
                    left = screenWidth - bubble.measuredWidth
                    builder.arrowPosition(shape.viewX+shape.width/2-left)
                    arrowCenter = false
                }
                // Check left
                if(left<0){
                    left = 0
                    builder.arrowPosition(shape.viewX+shape.width/2-left)
                    arrowCenter = false
                }
                bubbleHeight += builder.arrowHeight
            }
            is ArrowLocation.Left -> {
                left = (shape.viewX + shape.width + builder.bubbleMargin+builder.arrowHeight).toInt()
                top = (shape.viewY - (bubble.measuredHeight-shape.heigth)/2).toInt()
                bubbleWidth += builder.arrowHeight
            }
            is ArrowLocation.Right -> {
                left = (shape.viewX - builder.bubbleMargin - bubble.measuredWidth-builder.arrowHeight).toInt()
                top = (shape.viewY - (bubble.measuredHeight-shape.heigth)/2).toInt()
                bubbleWidth += builder.arrowHeight
            }
        }
        val layoutParams =  LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(left, top, 0, 0)
        bubble.layoutParams = layoutParams
        addView(bubble)
        // Add bubble background
        bubble.background = builder.rect(RectF(0f,0f, bubbleWidth, bubbleHeight)).arrowCenter(arrowCenter).build()

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