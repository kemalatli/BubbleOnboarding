package com.kemalatli.bubbleonboarding.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.kemalatli.bubbleonboarding.shape.base.FocalShape

class CircleFocalShape(view:View): FocalShape(view) {

    override fun draw(canvas: Canvas?, paint: Paint?) {
        if(canvas==null || paint==null) return
        val radius = width.coerceAtLeast(heigth) / 2
        canvas.drawCircle(viewX + width/2, viewY + heigth/2, radius, paint)
    }


}