package com.example.translatorapp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.example.translatorapp.databinding.ActivityMainBinding
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.selects.select

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var items = arrayOf("English", "Urdu", "Hindi", "Bangali", "French ")
    private var condition = DownloadConditions.Builder().requireWifi().build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val itemAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_dropdown_item_1line, items
        )

        binding.languageFrom.setAdapter(itemAdapter)

        binding.languageTo.setAdapter(itemAdapter)

        binding.translate.setOnClickListener {
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(selectFrom())
                .setTargetLanguage(selectTo())
                .build()

            val translator = Translation.getClient(options)
            translator.downloadModelIfNeeded(condition)
                .addOnSuccessListener {
                    translator.translate(binding.input.text.toString())
                        .addOnSuccessListener {
                            binding.output.text = it
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

        }

    }

    private fun selectFrom(): String {
        return when (binding.languageFrom.text.toString()) {
            "" -> TranslateLanguage.ENGLISH
            "English" -> TranslateLanguage.ENGLISH
            "Arabic" -> TranslateLanguage.ARABIC
            "Urdu" -> TranslateLanguage.URDU
            "Bangali" -> TranslateLanguage.BENGALI
            "French" -> TranslateLanguage.FRENCH

            else -> TranslateLanguage.ENGLISH

        }

    }

    private fun selectTo(): String {
        return when (binding.languageTo.text.toString()) {
            "" -> TranslateLanguage.URDU
            "English" -> TranslateLanguage.ENGLISH
            "Arabic" -> TranslateLanguage.ARABIC
            "Urdu" -> TranslateLanguage.URDU
            "Bangali" -> TranslateLanguage.BENGALI
            "French" -> TranslateLanguage.FRENCH

            else -> TranslateLanguage.URDU

        }

    }
} 