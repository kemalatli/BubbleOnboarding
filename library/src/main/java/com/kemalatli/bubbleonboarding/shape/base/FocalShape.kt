package com.kemalatli.bubbleonboarding.shape.base

import android.graphics.Canvas
import android.graphics.Paint
import android.view.View

abstract class FocalShape(private val view:View){

    val viewX:Float
    val viewY:Float
    val width:Float
    val heigth:Float

    init {
        val points = IntArray(2)
        view.getLocationOnScreen(points)
        viewX = points[0].toFloat()
        viewY = points[1].toFloat()
        width = view.width.toFloat()
        heigth = view.height.toFloat()
    }

    abstract fun draw(canvas: Canvas?, paint: Paint?)

}