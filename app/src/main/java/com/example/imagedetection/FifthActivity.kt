package com.example.imagedetection

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*

class FifthActivity : AppCompatActivity() {

    lateinit var progressBar: ProgressBar
    lateinit var toPicker: NumberPicker
    lateinit var fromPicker: NumberPicker
    lateinit var targetTextField: TextView
    lateinit var sourceTextField: EditText
    lateinit var btnTranslate: Button
    lateinit var btnDismiss: Button

    private var sourceLanguage = "a"
    private var targetLanguage = "a"

    var originalText: String = ""
    lateinit var translator: Translator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)

        progressBar = findViewById(R.id.progressBar)
        toPicker = findViewById(R.id.toPicker)
        fromPicker = findViewById(R.id.fromPicker)
        targetTextField = findViewById(R.id.targetTextField)
        sourceTextField = findViewById(R.id.sourceTextField)
        btnTranslate = findViewById(R.id.btnTranslate)
        btnDismiss = findViewById(R.id.btnDismiss)

        setUpPickers()
        progressBar.visibility = View.GONE

        btnTranslate.setOnClickListener {
            originalText = sourceTextField.text.toString()

            prepareTranslateModel()
        }
        btnDismiss.setOnClickListener {
            sourceTextField.setText(" ")
        }
    }

    private fun prepareTranslateModel() {
        val options: TranslatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(targetLanguage)
            .build()
        translator = Translation.getClient(options)

        translator.downloadModelIfNeeded().addOnSuccessListener {
            translateText()
        }.addOnFailureListener {
            targetTextField.text = "Error ${it.message}"
        }
    }

    private fun translateText() {
        translator.translate(originalText).addOnSuccessListener {
            targetTextField.text = it
        }.addOnFailureListener {
            targetTextField.text = "Error ${it.message}"
        }
    }


    private fun setUpPickers(){
        val data = arrayOf("None","English", "Swedish","German", "French")
        fromPicker.minValue = 0
        fromPicker.maxValue = data.size - 1
        fromPicker.displayedValues = data
        fromPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        fromPicker.setOnValueChangedListener{picker, oldvalue, newvalue ->

            sourceLanguage = getFirebaseLanguageFrom(data[newvalue])
        }

        toPicker.minValue = 0
        toPicker.maxValue = data.size - 1
        toPicker.displayedValues = data
        toPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        toPicker.setOnValueChangedListener{picker, oldvalue, newvalue ->

            targetLanguage = getFirebaseLanguageFrom(data[newvalue])
        }
    }
    private fun getFirebaseLanguageFrom(userSelection: String): String{

        when (userSelection){

            "English" -> return TranslateLanguage.ENGLISH
            "German"  -> return TranslateLanguage.GERMAN
            "French"  -> return TranslateLanguage.FRENCH
            "Swedish" -> return TranslateLanguage.SWEDISH
        }
        // Unknown
        return "a"

    }
}
