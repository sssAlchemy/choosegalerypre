package com.example.test.choosegallety

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import java.text.DecimalFormat
import com.squareup.picasso.Picasso

/**
 * Created by tsuboi on 2018/02/12.
 */
class DrawImage : AppCompatActivity() {

    private var imageView: TouchImageView? = null

    private var df: DecimalFormat? = null
    internal var bmp: Bitmap? = null
    internal var carpool_id: String? = "BST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_touchimageview)

        val imagePath = intent.getStringExtra("path")
        val bitmap = BitmapFactory.decodeFile(imagePath)

        val imageView = findViewById<ImageView>(R.id.img) as TouchImageView

//        Picasso.with(this@DrawImage)
//                .load(imagePath)
//                .placeholder(R.mipmap.ic_launcher)
//                .into(imageView)

        imageView.setImageBitmap(bitmap)

        imageView!!.setOnTouchImageViewListener(object : TouchImageView.OnTouchImageViewListener {
            override fun onMove() {
                val point = imageView!!.scrollPosition
                val rect = imageView!!.zoomedRect
                val currentZoom = imageView!!.currentZoom
                val isZoomed = imageView!!.isZoomed
            }
        })


    }
}