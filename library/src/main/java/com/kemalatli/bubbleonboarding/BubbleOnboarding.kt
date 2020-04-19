package com.kemalatli.bubbleonboarding

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.kemalatli.bubbleonboarding.background.BubbleBackgroundView
import com.kemalatli.bubbleonboarding.shape.CircleFocalShape
import com.kemalatli.bubbleonboarding.shape.RectangleFocalShape
import com.kemalatli.bubbleonboarding.shape.base.FocalShape

class BubbleOnboarding internal constructor():LifecycleObserver, View.OnClickListener {

    private var activity:Activity? = null
    private var lifecycle: Lifecycle? = null
    private var focalShape:FocalShape? = null
    private var backColor:Int = Color.parseColor("#dd335075")
    private var backgroundView: BubbleBackgroundView? = null

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

    fun liveIn(lifecycle:Lifecycle): BubbleOnboarding {
        this.lifecycle = lifecycle
        return this
    }

    fun show():BubbleOnboarding{
        activity.let {
            if(it==null) return@let
            backgroundView = BubbleBackgroundView(it)
            backgroundView?.focalShape = focalShape
            backgroundView?.maskColor = backColor
            backgroundView?.setOnClickListener(this)
            (it.window.decorView as ViewGroup?)?.addView(backgroundView)
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