package com.kemalatli.bubbleonboarding.shape

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.kemalatli.bubbleonboarding.shape.base.FocalShape

class RectangleFocalShape(view:View): FocalShape(view) {

    override fun draw(canvas: Canvas?, paint: Paint?) {
        if(canvas==null || paint==null) return
        canvas.drawRect(viewX, viewY,viewX+width,viewY+heigth, paint)
    }

}