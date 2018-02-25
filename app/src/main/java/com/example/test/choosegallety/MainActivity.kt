package com.example.test.choosegallety

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.provider.MediaStore
import android.provider.DocumentsContract
import android.support.annotation.RequiresApi
import android.widget.Toast
import android.widget.Button
import android.util.Log
import android.support.v4.content.ContextCompat
import android.Manifest

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_PERMISSION: Int = 1
        const val REQUEST_CODE_IMAGE_PICKER: Int = 6542
        @JvmField val PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("print-result_ok", "check")
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_CODE_PERMISSION
            )
        }

        // Kotlin
        // onCreate内
        val uploadImage = findViewById<Button>(R.id.button)
        uploadImage.setOnClickListener {
            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER)
            Toast.makeText(this, "Open Garally", Toast.LENGTH_LONG).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        when (requestCode) {
            REQUEST_CODE_IMAGE_PICKER -> {
                val imageData = data?.data ?: return
                val docID = DocumentsContract.getDocumentId(imageData)
                val strSplittedDocId = docID.split(":")
                val strId = strSplittedDocId[strSplittedDocId.size - 1]
                val cursor = contentResolver.query(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        arrayOf(MediaStore.MediaColumns.DATA),
                        "_id=?",
                        arrayOf<String>(strId),
                        null
                )
                cursor.moveToFirst()
                val imageURL: String = cursor.getString(0)
                Log.d("print-result_ok", imageURL)

                val intent: Intent = Intent(this, DrawImage::class.java)
                intent.putExtra("path", imageURL)
                startActivity(intent)
//                Toast.makeText(this,"URL; " + imageURL?:"",Toast.LENGTH_LONG)
            }
            else -> Log.d("onActivityResult when", "no match. code=$requestCode")
        }
    }
}
