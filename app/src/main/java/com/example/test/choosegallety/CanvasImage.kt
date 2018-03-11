package com.example.test.choosegallety

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.support.constraint.ConstraintLayout
import android.view.View
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener

/**
 * Created by tsuboi on 2018/03/04.
 */
class CanvasImage: AppCompatActivity() {
    private var canvasLayout: ConstraintLayout? = null
    private var point: Point = Point(0, 0)

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.canvas_image)

        canvasLayout = findViewById<ConstraintLayout>(R.id.constraintLayout) as android.support.constraint.ConstraintLayout
        val background = Canvass (this)
        canvasLayout!!.addView (background)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        point.set(canvasLayout!!.getWidth(), canvasLayout!!.getHeight())
        Log.d("point", point.x.toString())
        Log.d("layout", canvasLayout!!.getWidth().toString())
    }

    internal inner class Canvass @JvmOverloads constructor(context: Context): View (context) {
        private var _bitmap: Bitmap? = null
        private var _gestureDetector: ScaleGestureDetector? = null
        private var _scaleFactor = 1f

        private val _simpleListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                invalidate()
                return super.onScaleBegin(detector)
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {
                _scaleFactor *= detector.scaleFactor
                invalidate()
                super.onScaleEnd(detector)
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                _scaleFactor *= detector.scaleFactor
                invalidate()
                return true
            }
        }

        init {
            val imagePath: String = intent.getStringExtra("path")
            _bitmap = BitmapFactory.decodeFile(imagePath)

            _gestureDetector = ScaleGestureDetector(context, _simpleListener)
        }

        override fun onDraw (canvas: Canvas) {
            val imageWidth = _bitmap!!.getWidth()
            val imageHeight = _bitmap!!.getHeight()

            val widthRatio: Double = point.x.toDouble() / imageWidth.toDouble()
            val heightRatio: Double = point.y.toDouble() / imageHeight.toDouble()

            var width: Int;
            var height: Int;
            var top: Float = 0f
            var left: Float = 0f
            if (widthRatio <= heightRatio) {
                width = point.x
                height = (imageHeight * widthRatio).toInt()
                top = ((point.y - height) / 2).toFloat()
            } else {
                width = (imageWidth * heightRatio).toInt()
                height = point.y
                left = ((point.x - width) / 2).toFloat()
            }

            val resizeBitmap = Bitmap.createScaledBitmap(_bitmap , width, height, true)

            canvas.drawRGB (0, 0, 255)
            canvas.save()
            canvas.scale(_scaleFactor, _scaleFactor)
            canvas.drawBitmap (resizeBitmap, left, top, null)
            canvas.restore()
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            return _gestureDetector!!.onTouchEvent(event);
        }
    }
}