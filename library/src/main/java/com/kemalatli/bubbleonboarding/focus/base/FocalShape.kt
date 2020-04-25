package com.kemalatli.bubbleonboarding.focus.base

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import androidx.core.view.doOnLayout

abstract class FocalShape(val view:View){

    var viewX:Float = 0f
    var viewY:Float = 0f
    var width:Float = 0f
    var heigth:Float = 0f

    fun prepare(ready:()->Unit){
        view.doOnLayout {
            val points = IntArray(2)
            view.getLocationOnScreen(points)
            viewX = points[0].toFloat()
            viewY = points[1].toFloat()
            width = view.width.toFloat()
            heigth = view.height.toFloat()
            ready.invoke()
        }
    }

    abstract fun draw(canvas: Canvas?, paint: Paint?)

}