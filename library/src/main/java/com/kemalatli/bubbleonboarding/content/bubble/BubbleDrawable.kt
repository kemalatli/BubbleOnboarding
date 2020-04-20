package com.kemalatli.bubbleonboarding.content.bubble

import android.graphics.*
import android.graphics.drawable.Drawable

class BubbleDrawable: Drawable {

    private val rect: RectF?
    private val angle:Float
    private val arrowWidth:Float
    private val arrowHeight:Float
    private var arrowPosition:Float
    private val arrowLocation: ArrowLocation
    private var bubbleType: BubbleType
    private val arrowCenter:Boolean

    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private constructor(builder: Builder) {
        this.rect = builder.mRect
        this.angle = builder.angle
        this.arrowHeight = builder.arrowHeight
        this.arrowWidth = builder.arrowWidth
        this.arrowPosition = builder.arrowPosition
        this.arrowLocation = builder.mArrowLocation
        bubbleType = builder.bubbleType
        this.arrowCenter = builder.arrowCenter
    }


    override fun draw(canvas: Canvas) {
        val type = bubbleType
        when (type) {
            is BubbleType.SolidColor -> paint.color = type.color
        }
        setUpPath(arrowLocation, path)
        canvas.drawPath(path, paint)
    }

    private fun setUpPath(mArrowLocation: ArrowLocation, path: Path) {
        val rectangle = rect ?: return
        when (mArrowLocation) {
            is ArrowLocation.Left -> setUpLeftPath(rectangle, path)
            is ArrowLocation.Right -> setUpRightPath(rectangle, path)
            is ArrowLocation.Top -> setUpTopPath(rectangle, path)
            is ArrowLocation.Bottom -> setUpBottomPath(rectangle, path)
        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    override fun getIntrinsicHeight(): Int = rect?.height()?.toInt() ?: 0

    override fun getIntrinsicWidth(): Int = rect?.width()?.toInt() ?: 0

    private fun setUpLeftPath(rect: RectF, path: Path) {
        if (arrowCenter) {
            arrowPosition = (rect.bottom - rect.top) / 2 - arrowWidth / 2
        }
        path.moveTo(arrowWidth + rect.left + angle, rect.top)
        path.lineTo(rect.width() - angle, rect.top)
        path.arcTo(RectF(rect.right - angle, rect.top, rect.right, angle + rect.top), 270f, 90f)
        path.lineTo(rect.right, rect.bottom - angle)
        path.arcTo(RectF(rect.right - angle, rect.bottom - angle, rect.right, rect.bottom), 0f, 90f)
        path.lineTo(rect.left + arrowWidth + angle, rect.bottom)
        path.arcTo(RectF(rect.left + arrowWidth, rect.bottom - angle, angle + rect.left + arrowWidth, rect.bottom), 90f, 90f)
        path.lineTo(rect.left + arrowWidth, arrowHeight + arrowPosition)
        path.lineTo(rect.left, arrowPosition + arrowHeight / 2)
        path.lineTo(rect.left + arrowWidth, arrowPosition)
        path.lineTo(rect.left + arrowWidth, rect.top + angle)
        path.arcTo(RectF(rect.left + arrowWidth, rect.top, angle + rect.left + arrowWidth, angle + rect.top), 180f, 90f)
        path.close()
    }

    private fun setUpTopPath(rect: RectF, path: Path) {
        if (arrowCenter) {
            arrowPosition = (rect.right - rect.left) / 2 - arrowWidth / 2
        }
        path.moveTo(rect.left + Math.min(arrowPosition, angle), rect.top + arrowHeight)
        path.lineTo(rect.left + arrowPosition, rect.top + arrowHeight)
        path.lineTo(rect.left + arrowWidth / 2 + arrowPosition, rect.top)
        path.lineTo(rect.left + arrowWidth + arrowPosition, rect.top + arrowHeight)
        path.lineTo(rect.right - angle, rect.top + arrowHeight)
        path.arcTo(RectF(rect.right - angle, rect.top + arrowHeight, rect.right, angle + rect.top + arrowHeight), 270f, 90f)
        path.lineTo(rect.right, rect.bottom - angle)
        path.arcTo(RectF(rect.right - angle, rect.bottom - angle, rect.right, rect.bottom), 0f, 90f)
        path.lineTo(rect.left + angle, rect.bottom)
        path.arcTo(RectF(rect.left, rect.bottom - angle, angle + rect.left, rect.bottom), 90f, 90f)
        path.lineTo(rect.left, rect.top + arrowHeight + angle)
        path.arcTo(RectF(rect.left, rect.top + arrowHeight, angle + rect.left, angle + rect.top + arrowHeight), 180f, 90f)
        path.close()
    }

    private fun setUpRightPath(rect: RectF, path: Path) {
        if (arrowCenter) {
            arrowPosition = (rect.bottom - rect.top) / 2 - arrowWidth / 2
        }
        path.moveTo(rect.left + angle, rect.top)
        path.lineTo(rect.width() - angle - arrowWidth, rect.top)
        path.arcTo(RectF(rect.right - angle - arrowWidth, rect.top, rect.right - arrowWidth, angle + rect.top), 270f, 90f)
        path.lineTo(rect.right - arrowWidth, arrowPosition)
        path.lineTo(rect.right, arrowPosition + arrowHeight / 2)
        path.lineTo(rect.right - arrowWidth, arrowPosition + arrowHeight)
        path.lineTo(rect.right - arrowWidth, rect.bottom - angle)
        path.arcTo(RectF(rect.right - angle - arrowWidth, rect.bottom - angle, rect.right - arrowWidth, rect.bottom), 0f, 90f)
        path.lineTo(rect.left + arrowWidth, rect.bottom)
        path.arcTo(RectF(rect.left, rect.bottom - angle, angle + rect.left, rect.bottom), 90f, 90f)
        path.arcTo(RectF(rect.left, rect.top, angle + rect.left, angle + rect.top), 180f, 90f)
        path.close()
    }

    private fun setUpBottomPath(rect: RectF, path: Path) {
        if (arrowCenter) {
            arrowPosition = (rect.right - rect.left) / 2 - arrowWidth / 2
        }
        path.moveTo(rect.left + angle, rect.top)
        path.lineTo(rect.width() - angle, rect.top)
        path.arcTo(RectF(rect.right - angle, rect.top, rect.right, angle + rect.top), 270f, 90f)
        path.lineTo(rect.right, rect.bottom - arrowHeight - angle)
        path.arcTo(RectF(rect.right - angle, rect.bottom - angle - arrowHeight, rect.right, rect.bottom - arrowHeight), 0f, 90f)
        path.lineTo(rect.left + arrowWidth + arrowPosition, rect.bottom - arrowHeight)
        path.lineTo(rect.left + arrowPosition + arrowWidth / 2, rect.bottom)
        path.lineTo(rect.left + arrowPosition, rect.bottom - arrowHeight)
        path.lineTo(rect.left + Math.min(angle, arrowPosition), rect.bottom - arrowHeight)
        path.arcTo(RectF(rect.left, rect.bottom - angle - arrowHeight, angle + rect.left, rect.bottom - arrowHeight), 90f, 90f)
        path.lineTo(rect.left, rect.top + angle)
        path.arcTo(RectF(rect.left, rect.top, angle + rect.left, angle + rect.top), 180f, 90f)
        path.close()
    }


    class Builder {

        internal var mRect: RectF? = null
        internal var arrowWidth = DEFAULT_ARROW_WITH
        internal var angle = DEFAULT_ANGLE
        internal var arrowHeight = DEFAULT_ARROW_HEIGHT
        internal var arrowPosition = DEFAULT_ARROW_POSITION
        internal var bubbleType: BubbleType = BubbleType.SolidColor(Color.RED)
        internal var mArrowLocation: ArrowLocation = ArrowLocation.Bottom()
        internal var arrowCenter = false

        fun rect(rect: RectF?): Builder {
            mRect = rect
            return this
        }

        fun arrowWidth(arrowWidth: Float): Builder {
            this.arrowWidth = arrowWidth
            return this
        }

        fun angle(angle: Float): Builder {
            this.angle = angle * 2
            return this
        }

        fun arrowHeight(arrowHeight: Float): Builder {
            this.arrowHeight = arrowHeight
            return this
        }

        fun arrowPosition(arrowPosition: Float): Builder {
            this.arrowPosition = arrowPosition
            return this
        }

        fun arrowLocation(arrowLocation: ArrowLocation): Builder {
            mArrowLocation = arrowLocation
            return this
        }

        fun bubbleType(bubbleType: BubbleType): Builder {
            this.bubbleType = bubbleType
            return this
        }

        fun arrowCenter(arrowCenter: Boolean): Builder {
            this.arrowCenter = arrowCenter
            return this
        }

        fun build(): BubbleDrawable {
            requireNotNull(mRect) { "Should define the bubble rectangle" }
            return BubbleDrawable(this)
        }

        companion object {
            var DEFAULT_ARROW_WITH = 25f
            var DEFAULT_ARROW_HEIGHT = 25f
            var DEFAULT_ANGLE = 20f
            var DEFAULT_ARROW_POSITION = 50f
        }
    }


}