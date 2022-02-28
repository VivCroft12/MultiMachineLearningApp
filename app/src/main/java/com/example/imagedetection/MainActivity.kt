package com.example.imagedetection

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var btnChooseImgDet: Button
    lateinit var btnChooseTextDet: Button
    lateinit var btnDetectLang: Button
    lateinit var btnTranslate: Button

    private val CAMERA_PERMISSION_CODE = 123
    private val READ_STORAGE_PERMISSION_CODE = 113
    private val WRITE_STORAGE_PERMISSION_CODE = 113

    private val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnChooseImgDet = findViewById(R.id.btnChooseImgDet)
        btnChooseTextDet = findViewById(R.id.btnChooseTextDet)
        btnDetectLang = findViewById(R.id.btnDetectLang)
        btnTranslate = findViewById(R.id.btnTranslate)

        btnChooseImgDet.setOnClickListener {
            Intent(this, SecondActivity::class.java).also {
                startActivity(it)
            }
        }
        btnChooseTextDet.setOnClickListener {
            Intent(this, ThirdActivity::class.java).also {
                startActivity(it)
            }
        }
        btnDetectLang.setOnClickListener {
            Intent(this, FourthActivity::class.java).also {
                startActivity(it)
            }
        }
        btnTranslate.setOnClickListener {
            Intent(this, FifthActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        checkPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    READ_STORAGE_PERMISSION_CODE
                )
            }
            else {
                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        else if(requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    WRITE_STORAGE_PERMISSION_CODE
                )
            } else {
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        else if(requestCode == WRITE_STORAGE_PERMISSION_CODE) {
            if (!grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}