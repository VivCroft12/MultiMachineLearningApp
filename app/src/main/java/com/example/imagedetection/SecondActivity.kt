package com.example.imagedetection

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.lang.Exception

class SecondActivity : AppCompatActivity() {

    lateinit var ivPicture: ImageView
    lateinit var tvResult: TextView
    lateinit var btnChoosePicture: Button

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    lateinit var inputImage: InputImage

    lateinit var imagelabeler: ImageLabeler
    private val TAG = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        ivPicture = findViewById(R.id.ivPicture)
        tvResult = findViewById(R.id.tvResult)
        btnChoosePicture = findViewById(R.id.btnChoosePicture)

        imagelabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object: ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        val photo = data?.extras?.get("data") as Bitmap
                        ivPicture.setImageBitmap(photo)
                        inputImage = InputImage.fromBitmap(photo, 0)
                        processImage()
                    }
                    catch(e: Exception) {
                        Log.d(TAG, "onActivityResult ${e.message}")
                    }
                }
            }
        )

        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            object: ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    val data = result?.data
                    try {
                        inputImage = InputImage.fromFilePath(this@SecondActivity, data?.data!!)
                        ivPicture.setImageURI(data?.data)
                        processImage()
                    }
                    catch(e: Exception) {

                    }
                }
            }
        )

        btnChoosePicture.setOnClickListener {
            val options = arrayOf("camera", "gallery")
            val builder = AlertDialog.Builder(this@SecondActivity)
            builder.setTitle("Pick an option")

            builder.setItems(options, DialogInterface.OnClickListener {
                    dialog, which ->
                if(which == 0) {
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(cameraIntent)
                }
                else {
                    val storageIntent = Intent()
                    storageIntent.setType("image/*")
                    storageIntent.setAction(Intent.ACTION_GET_CONTENT)
                    galleryLauncher.launch(storageIntent)
                }
            })
            builder.show()
        }
    }

    private fun processImage() {
        imagelabeler.process(inputImage)
            .addOnSuccessListener {
                var result = ""

                for(label in it) {
                    val text = label.text
                    val confidence = label.confidence
                    val index = label.index
                    result = result + "\n" + text + " " + confidence
                }

                tvResult.text = result
            }
            .addOnFailureListener {
                Log.d(TAG, "processImage: ${it.message}")
            }
    }
}