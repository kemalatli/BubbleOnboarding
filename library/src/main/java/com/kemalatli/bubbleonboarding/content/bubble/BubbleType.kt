package com.kemalatli.bubbleonboarding.content.bubble

sealed class BubbleType {

    class SolidColor(val color:Int): BubbleType()
    class GradientColor(): BubbleType()

}