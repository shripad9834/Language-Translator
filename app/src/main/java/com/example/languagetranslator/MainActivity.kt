package com.example.languagetranslator

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    var txtUsername: TextView? = null
    private lateinit var fSpinner: Spinner
    private lateinit var tSpinner: Spinner
    private lateinit var sourceText: TextInputEditText
    private lateinit var mic: ImageView
    private lateinit var translate: MaterialButton
    private lateinit var translated: TextView
//    private lateinit var on: ImageView
//    private lateinit var off: ImageView
private lateinit var toggleSourceTTS: ImageView
    private lateinit var toggleTranslatedTTS: ImageView
//    private var isSpeaking = false // Track speaking state

    var isSourceSpeaking = false
    var isTranslatedSpeaking = false
//    private lateinit var translatedon: ImageView
//    private lateinit var translatedoff: ImageView
    private var tts: TextToSpeech? = null

    private val fromLanguages = arrayOf("From", "English", "Marathi", "Hindi", "Telugu", "Arabic", "Bengali", "Urdu")
    private val toLanguages = arrayOf("To", "English", "Marathi", "Hindi", "Telugu", "Arabic", "Bengali", "Urdu")

    companion object {
        private const val REQUEST_PERMISSION_CODE = 1
    }

    private var fromLanguageCode = 0
    private var toLanguageCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        txtUsername = findViewById(R.id.user_details);

        if (user == null) {
            val i = Intent(applicationContext, Login::class.java)
            startActivity(i)
            finish()
        } else {
            var username = user!!.displayName // Get username from Firebase
            if (username == null || username.isEmpty()) {
                username = "User" // Default if no username is set
            }
            txtUsername!!.text = "Welcome, $username!"
        }

        fSpinner = findViewById(R.id.spinnerFrom)
        tSpinner = findViewById(R.id.spinnerTo)
        sourceText = findViewById(R.id.input)
        mic = findViewById(R.id.microphone)
        translate = findViewById(R.id.BtnTranslate)
        translated = findViewById(R.id.TranslatedText)
//        on = findViewById(R.id.on)
//        off = findViewById(R.id.off)
//        translatedon = findViewById(R.id.translatedon)
//        translatedoff = findViewById(R.id.translatedoff)

        toggleSourceTTS = findViewById(R.id.toggleTTS)
        toggleTranslatedTTS = findViewById(R.id.toggleTranslatedTTS)

        // Initialize TTS
        initTTS()




        toggleSourceTTS.setOnClickListener {
            if (isSourceSpeaking) {
                stopSpeaking(true)  // Stops only source TTS
            } else {
                startSpeaking(sourceText.text.toString(), fSpinner, true)
            }
        }

        toggleTranslatedTTS.setOnClickListener {
            if (isTranslatedSpeaking) {
                stopSpeaking(false) // Stops only translated TTS
            } else {
                startSpeaking(translated.text.toString(), tSpinner, false)
            }
        }


        val fromAdapter = ArrayAdapter(this, R.layout.spinner_item, fromLanguages)
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fSpinner.adapter = fromAdapter

        fSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                fromLanguageCode = getLanguageCode(fromLanguages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        val toAdapter = ArrayAdapter(this, R.layout.spinner_item, toLanguages)
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tSpinner.adapter = toAdapter

        tSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                toLanguageCode = getLanguageCode(toLanguages[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        translate.setOnClickListener {
            translated.text = ""
            when {
                sourceText.text.toString().isEmpty() -> Toast.makeText(this, "Please Enter Something!", Toast.LENGTH_SHORT).show()
                fromLanguageCode == 0 -> Toast.makeText(this, "Please Select Source Language!", Toast.LENGTH_SHORT).show()
                toLanguageCode == 0 -> Toast.makeText(this, "Please Select the Target Language!", Toast.LENGTH_SHORT).show()
                else -> translateText(fromLanguageCode, toLanguageCode, sourceText.text.toString())
            }
        }

//        on.setOnClickListener { startSpeaking(fSpinner) }
//
//        off.setOnClickListener { stopSpeaking() }

//        translatedon.setOnClickListener { startSpeaking(tSpinner) }
//
//        translatedoff.setOnClickListener { stopSpeaking() }
        mic.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to translate")
            startActivityForResult(intent, REQUEST_PERMISSION_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_PERMISSION_CODE && resultCode == RESULT_OK && data != null) {
            val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            sourceText.setText(result?.get(0))
        }
    }

    private fun translateText(fromLanguageCode: Int, toLanguageCode: Int, text: String) {
        translated.text = "Downloading Language..."
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(fromLanguageCode)
            .setTargetLanguage(toLanguageCode)
            .build()

        val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
        val conditions = FirebaseModelDownloadConditions.Builder().build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translated.text = "Translating..."
                translator.translate(text)
                    .addOnSuccessListener { translatedText -> translated.text = translatedText }
                    .addOnFailureListener { e -> Toast.makeText(this, "Translation failed: ${e.message}", Toast.LENGTH_SHORT).show() }
            }
            .addOnFailureListener { e -> Toast.makeText(this, "Model download failed: ${e.message}", Toast.LENGTH_SHORT).show() }
    }

    private fun getLanguageCode(language: String): Int {
        return when (language) {
            "English" -> FirebaseTranslateLanguage.EN
            "Marathi" -> FirebaseTranslateLanguage.MR
            "Hindi" -> FirebaseTranslateLanguage.HI
            "Telugu" -> FirebaseTranslateLanguage.TE
            "Arabic" -> FirebaseTranslateLanguage.AR
            "Bengali" -> FirebaseTranslateLanguage.BN
            "Urdu" -> FirebaseTranslateLanguage.UR
            else -> 0
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    private fun initTTS() {
        tts = TextToSpeech(applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            } else {
                Toast.makeText(this, "TTS Initialization Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun startSpeaking(text: String, spinner: Spinner, isSource: Boolean) {
        if (text.isEmpty()) {
            Toast.makeText(this, "Text is empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Stop the other TTS if it's playing
        if (isSource && isTranslatedSpeaking) stopSpeaking(false)
        if (!isSource && isSourceSpeaking) stopSpeaking(true)

        val selectedLocale = getLocaleFromPosition(spinner.selectedItemPosition)
        tts?.language = selectedLocale
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)

        // Set the correct flag
        if (isSource) {
            isSourceSpeaking = true
            toggleSourceTTS.setImageResource(R.drawable.volume_727269)
        } else {
            isTranslatedSpeaking = true
            toggleTranslatedTTS.setImageResource(R.drawable.volume_727269)
        }
    }



    private fun stopSpeaking(isSource: Boolean) {
        tts?.stop()

        if (isSource) {
            isSourceSpeaking = false
            toggleSourceTTS.setImageResource(R.drawable.volume)
        } else {
            isTranslatedSpeaking = false
            toggleTranslatedTTS.setImageResource(R.drawable.volume)
        }
    }


    private fun getLocaleFromPosition(position: Int): Locale {
        return when (position) {
            1 -> Locale.ENGLISH
            2 -> Locale("mr") // Marathi
            3 -> Locale("hi") // Hindi
            4 -> Locale("te") // Telugu
            5 -> Locale("ar") // Arabic
            6 -> Locale("bn") // Bengali
            7 -> Locale("ur") // Urdu
            else -> Locale.ENGLISH
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == "Logout") {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(applicationContext, Login::class.java))
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}