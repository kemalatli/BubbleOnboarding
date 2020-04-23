package com.kemalatli.bubbleonboarding

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kemalatli.bubbleonboarding.background.BubbleBackgroundView
import com.kemalatli.bubbleonboarding.content.bubble.ArrowLocation
import com.kemalatli.bubbleonboarding.content.bubble.BubbleDrawable
import com.kemalatli.bubbleonboarding.content.bubble.BubbleType
import com.kemalatli.bubbleonboarding.focus.CircleFocalShape
import com.kemalatli.bubbleonboarding.focus.RectangleFocalShape
import com.kemalatli.bubbleonboarding.focus.base.FocalShape

class BubbleOnboarding internal constructor():LifecycleObserver, View.OnClickListener {

    @ColorInt
    private var backColor:Int = Color.parseColor("#dd335075")
    @LayoutRes
    private var customViewRes:Int = R.layout.view_bubble
    private var activity:Activity? = null
    private var lifecycle: Lifecycle? = null
    private var focalShape:FocalShape? = null
    private var backgroundView: BubbleBackgroundView? = null
    private var arrowWidth = BubbleDrawable.Builder.DEFAULT_ARROW_WITH
    private var angle = BubbleDrawable.Builder.DEFAULT_ANGLE
    private var arrowHeight = BubbleDrawable.Builder.DEFAULT_ARROW_HEIGHT
    private var arrowLocation: ArrowLocation = ArrowLocation.Bottom()
    private var bubbleType:BubbleType = BubbleType.SolidColor(Color.YELLOW)
    private var bubbleMargin:Int = 20

    companion object{
        fun with(activity: Activity): BubbleOnboarding {
            val bubbleOnboarding = BubbleOnboarding()
            bubbleOnboarding.activity=activity
            return bubbleOnboarding
        }
    }

    fun focusInCircle(target:View): BubbleOnboarding {
        this.focalShape = CircleFocalShape(target)
        return this
    }

    fun focusInRectangle(target:View): BubbleOnboarding {
        this.focalShape = RectangleFocalShape(target)
        return this
    }

    fun focusInShape(focalShape: FocalShape): BubbleOnboarding {
        this.focalShape = focalShape
        return this
    }

    fun backColor(@ColorInt color:Int): BubbleOnboarding {
        this.backColor = color
        return this
    }

    fun customViewRes(@LayoutRes customViewRes:Int): BubbleOnboarding {
        this.customViewRes = customViewRes
        return this
    }

    fun liveIn(lifecycle:Lifecycle): BubbleOnboarding {
        this.lifecycle = lifecycle
        return this
    }

    fun arrowWidth(arrowWidth: Float): BubbleOnboarding {
        this.arrowWidth = arrowWidth
        return this
    }

    fun angle(angle: Float): BubbleOnboarding {
        this.angle = angle * 2
        return this
    }

    fun arrowHeight(arrowHeight: Float): BubbleOnboarding {
        this.arrowHeight = arrowHeight
        return this
    }

    fun bubbleMargin(bubbleMargin: Int): BubbleOnboarding {
        this.bubbleMargin = bubbleMargin
        return this
    }

    fun arrowLocation(arrowLocation: ArrowLocation): BubbleOnboarding {
        this.arrowLocation = arrowLocation
        return this
    }

    fun bubbleType(bubbleType: BubbleType): BubbleOnboarding {
        this.bubbleType = bubbleType
        return this
    }

    fun show():BubbleOnboarding{
        activity.let {
            if(it==null) return@let
            // Lifecycle
            this.lifecycle?.addObserver(this)
            // Background View
            backgroundView = BubbleBackgroundView(it)
            backgroundView?.focalShape = focalShape
            backgroundView?.maskColor = backColor
            backgroundView?.setOnClickListener(this)
            (it.window.decorView as ViewGroup?)?.addView(backgroundView)
            // Informative view
            val bubble = LayoutInflater.from(it).inflate(customViewRes, null)
            val builder = BubbleDrawable.Builder()
                .angle(angle)
                .arrowWidth(arrowWidth)
                .arrowHeight(arrowHeight)
                .arrowLocation(arrowLocation)
                .bubbleMargin(bubbleMargin)
                .bubbleType(bubbleType)
            backgroundView?.addBubble(bubble, builder)
        }
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear(){
        (activity?.window?.decorView as ViewGroup?)?.removeView(backgroundView)
        activity = null
        focalShape = null
    }

    override fun onClick(v: View?) {
        clear()
    }

}