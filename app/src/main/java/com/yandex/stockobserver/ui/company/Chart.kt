package com.yandex.stockobserver.ui.company

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.yandex.stockobserver.genralInfo.StockCandle


class Chart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
    defAttributeSet: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defAttributeSet, defStyleRes) {
    private lateinit var data: StockCandle
    private var originX = 0f // current position of viewport
    private var originY = 0f
    private val gestureDetector = GestureDetector(context, CsCvGestureDetector())

    inner class CsCvGestureDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, dx: Float, dy: Float): Boolean {
            Log.d("Chart", "onScroll: " + dx + " " + dy)
            originX += dx
            originY += dy
            invalidate()
            return true
        }

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            return false
        }
    }

    init {
        isHorizontalScrollBarEnabled = true
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawPath(canvas)
        drawTimeline(canvas)
    }

    fun setData(data: StockCandle) {
        this.data = data
        invalidate()
    }

    private fun drawPath(canvas: Canvas) {
        if (this::data.isInitialized) {
            val widthF = width.toFloat()
            val heightF = height.toFloat()
            val offsetX = (width / data.openPrice.size)// it can be used for scale
            val shader = createShader()
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG).apply {
                setShader(shader)
            }

            val paintStroke = Paint().apply {
                color = Color.BLACK
                style = Paint.Style.STROKE
                strokeWidth = 8f
            }
            //canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            var lowestValue = 1000000000.0
            data.lowPrice.forEach {
                if (it < lowestValue)
                    lowestValue = it
            }

            var higherValue = 0.0
            data.highPrice.forEach {
                if (it > higherValue)
                    higherValue = it
            }


            val path = Path()
            path.moveTo(0f, heightF)
            data.openPrice.forEachIndexed { index, _openPrice ->
                val coordY = getYCoordFromPrice(_openPrice, lowestValue, higherValue, height - 200)
                val coordX = if (originX + widthF < widthF)
                    ((index) * offsetX)
                else
                    ((index + (originX / 2)) * offsetX)

                Log.d("TAG", "drawPath: " + coordX + " " + coordY)
                if (index == 0) {
                    path.lineTo(coordX.toFloat(), coordY)
                } else
                    path.lineTo(coordX.toFloat(), coordY)
                if (index == data.openPrice.size - 1) {
                    path.lineTo(widthF, coordY)
                    path.lineTo(widthF, heightF)
                }
            }

            canvas.drawPath(path, paint)
            canvas.drawPath(path, paintStroke)
        } else {
            Log.d("chart", "drawPath: data not init")
        }
    }


    private fun getYCoordFromPrice(
        price: Double,
        lowestValue: Double,
        higherValue: Double,
        height: Int
    ): Float {
        return (height + ((price - lowestValue) / (higherValue - lowestValue) * -height)).toFloat()
    }

    private fun drawTimeline(canvas: Canvas) {
        val w = width - originX
        val count = 100f
        val bottomY = height - 60
        val topY = height - 120
        val offsetX = (width / 10)  //it can be used for scale
        val paint = Paint().apply {
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 5f
        }

        val textBoundRect = Rect()
        val textPaint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            isAntiAlias = true
            textSize = 30f
        }

        val points = mutableListOf<Float>()
        for (i in 0 until count.toInt()) {
            val text = "12" + i
            textPaint.getTextBounds(text, 0, text.length, textBoundRect)

            points.add(((i + (originX / 4)) * offsetX))
            points.add(bottomY.toFloat())
            points.add(((i + (originX / 4)) * offsetX))
            points.add(topY.toFloat())
            val textWidth = textPaint.measureText(text);
            val textHeight = textBoundRect.height();

            canvas.drawText(
                text,
                (i + (originX / 4)) * offsetX - (textWidth / 2f),
                bottomY + textHeight + (textHeight / 2f),
                textPaint
            )
        }
        canvas.drawLines(points.toFloatArray(), paint)
    }

    private fun createShader(): Shader {
        return LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            Color.GRAY, Color.WHITE, Shader.TileMode.MIRROR)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (gestureDetector.onTouchEvent(event)) return true
        return super.onTouchEvent(event);
    }
}
