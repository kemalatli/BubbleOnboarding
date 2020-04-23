package com.kemalatli.bubbleonboarding.content.bubble

sealed class BubbleType {

    class SolidColor(val color:Int): BubbleType()
    class GradientColor(val startColor:Int, val endColor:Int, val ange:Float): BubbleType()

}