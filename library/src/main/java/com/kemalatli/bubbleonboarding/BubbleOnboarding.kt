package com.kemalatli.bubbleonboarding

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import androidx.core.view.doOnLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
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
    private var bubbleId:String = "key"

    private var title:String = ""
    @StyleRes
    private var titleTextAppearance:Int = R.style.TextAppearance_AppCompat_SearchResult_Title
    private var subtitle:String = ""
    @StyleRes
    private var subtitleTextAppearance:Int = R.style.TextAppearance_AppCompat_SearchResult_Subtitle
    private var okLabel:String = ""
    @StyleRes
    private var okLabelTextAppearance:Int = R.style.TextAppearance_AppCompat_Caption
    private var cancelLabel:String = ""
    @StyleRes
    private var cancelLabelTextAppearance:Int = R.style.TextAppearance_AppCompat_Caption

    private var customViewUsed:Boolean = false
    private var showOnce:Boolean = false

    companion object{

        internal var isShowing:Boolean = false

        fun with(activity: Activity): BubbleOnboarding {
            val bubbleOnboarding = BubbleOnboarding()
            bubbleOnboarding.activity=activity
            return bubbleOnboarding
        }

        fun wasShownBefore(context:Context, bubbleId: String): Boolean {
            val sharedPreference = context.getSharedPreferences("bubbles", Context.MODE_PRIVATE)
            return sharedPreference.getBoolean(bubbleId, false)
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
        customViewUsed = true
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

    fun title(title: String): BubbleOnboarding {
        this.title = title
        return this
    }

    fun subtitle(subtitle: String): BubbleOnboarding {
        this.subtitle = subtitle
        return this
    }

    fun okLabel(okLabel: String): BubbleOnboarding {
        this.okLabel = okLabel
        return this
    }

    fun cancelLabel(cancelLabel: String): BubbleOnboarding {
        this.cancelLabel = cancelLabel
        return this
    }

    fun titleTextAppearance(titleTextAppearance: Int): BubbleOnboarding {
        this.titleTextAppearance= titleTextAppearance
        return this
    }

    fun subtitleTextAppearance(subtitleTextAppearance: Int): BubbleOnboarding {
        this.subtitleTextAppearance = subtitleTextAppearance
        return this
    }

    fun okLabelTextAppearance(okLabelTextAppearance: Int): BubbleOnboarding {
        this.okLabelTextAppearance = okLabelTextAppearance
        return this
    }

    fun cancelLabelTextAppearance(cancelLabelTextAppearance: Int): BubbleOnboarding {
        this.cancelLabelTextAppearance = cancelLabelTextAppearance
        return this
    }

    fun showOnce(bubbleId:String): BubbleOnboarding {
        this.showOnce = true
        this.bubbleId = bubbleId
        return this
    }

    fun show():BubbleOnboarding{
        activity.let {

            // Check
            if(isShowing) return@let
            requireNotNull(it)
            requireNotNull(focalShape)

            // Check if the balloon was shown before
            val sharedPreference = it.getSharedPreferences("bubbles", Context.MODE_PRIVATE)
            if(showOnce && sharedPreference.getBoolean(bubbleId, false)){
                return@let
            }

            // Wait for view layout
            focalShape?.prepare {
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
                // Set texts and styles
                if(!customViewUsed){
                    bubble.findViewById<MaterialTextView>(R.id.title).apply {
                        text = title
                        setTextAppearance(context, titleTextAppearance)
                    }
                    bubble.findViewById<MaterialTextView>(R.id.subtitle).apply {
                        text = subtitle
                        setTextAppearance(context, subtitleTextAppearance)
                    }
                    bubble.findViewById<MaterialButton>(R.id.ok).apply {
                        text = okLabel
                        setTextAppearance(context, okLabelTextAppearance)
                        setOnClickListener { clear() }
                    }
                    bubble.findViewById<MaterialButton>(R.id.cancel).apply {
                        text = cancelLabel
                        setTextAppearance(context, cancelLabelTextAppearance)
                        setOnClickListener { clear() }
                    }
                }
                // Build
                val builder = BubbleDrawable.Builder()
                    .angle(angle)
                    .arrowWidth(arrowWidth)
                    .arrowHeight(arrowHeight)
                    .arrowLocation(arrowLocation)
                    .bubbleMargin(bubbleMargin)
                    .bubbleType(bubbleType)
                backgroundView?.addBubble(bubble, builder)
                isShowing = true
                sharedPreference.edit().putBoolean(bubbleId, true).apply()
            }
        }
        return this
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun clear(){
        (activity?.window?.decorView as ViewGroup?)?.removeView(backgroundView)
        activity = null
        focalShape = null
        isShowing = false
    }

    override fun onClick(v: View?) {
        clear()
    }

}