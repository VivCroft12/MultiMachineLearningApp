package com.example.imagedetection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.google.mlkit.nl.smartreply.*
import java.util.*

class FourthActivity : AppCompatActivity() {
    lateinit var etInput: EditText
    lateinit var tvDetected: TextView
    lateinit var btnEnter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)

        etInput = findViewById(R.id.eTInput)
        tvDetected = findViewById(R.id.tvDetected)
        btnEnter = findViewById(R.id.btnEnter)

        btnEnter.setOnClickListener {
            val langText: String = etInput.text.toString()
            if(langText.equals(""))
            {
                Toast.makeText(this@FourthActivity, "Please enter some text", Toast.LENGTH_LONG).show()
            }
            else
            {
                detectLang(langText)
            }
        }
    }

    private fun detectLang(langText: String) {
        val langIdentifier: LanguageIdentifier = LanguageIdentification.getClient()

        langIdentifier.identifyLanguage(langText)
            .addOnSuccessListener { languageCode ->
                if(languageCode == "und") {
                    tvDetected.text = "Can't identify the language"
                }
                else {
                    val loc = Locale(languageCode)
                    val ans = loc.getDisplayLanguage()
                    tvDetected.text = "Language: $ans"
                }
            }
            .addOnFailureListener {
                tvDetected.text = "Exception ${it.message}"
            }
    }
}