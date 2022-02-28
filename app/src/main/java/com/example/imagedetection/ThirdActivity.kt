package com.example.imagedetection

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions


class ThirdActivity : AppCompatActivity() {

    lateinit var tvTextResult: TextView
    lateinit var btnChoosePic: Button

    var intentActivityResultLauncher: ActivityResultLauncher<Intent>? = null

    lateinit var inputImage: InputImage
    lateinit var textRecognizer: TextRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        tvTextResult = findViewById(R.id.tvTextResult)
        btnChoosePic = findViewById(R.id.btnChoosePic)

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        intentActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {
                val data = it.data
                val imageUri = data?.data

                convertImageToText(imageUri)
            }
        )
        btnChoosePic.setOnClickListener {
            val chooseIntent = Intent()
            chooseIntent.type = "image/*"
            chooseIntent.action = Intent.ACTION_GET_CONTENT
            intentActivityResultLauncher?.launch(chooseIntent)
        }
    }

    private fun convertImageToText(imageUri: Uri?) {
        try {
            inputImage = InputImage.fromFilePath(applicationContext, imageUri!!)

            val result: Task<Text> = textRecognizer.process(inputImage)
                .addOnSuccessListener {
                    tvTextResult.text = it.text
                }
                .addOnFailureListener {
                    tvTextResult.text = "Error : ${it.message}"
                }
        }
        catch(e:Exception) {

        }
    }

}