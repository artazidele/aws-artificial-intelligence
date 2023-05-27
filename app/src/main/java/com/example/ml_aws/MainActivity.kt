package com.example.ml_aws

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.amplifyframework.core.Amplify
import com.amplifyframework.predictions.PredictionsException
import java.io.*

class MainActivity : AppCompatActivity() {

    private val mp = MediaPlayer()

    private lateinit var textBtn: Button
    private lateinit var translateBtn: Button
    private lateinit var textV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textBtn = findViewById(R.id.text_to_speech_btn)
        translateBtn = findViewById(R.id.translate_text_btn)
        textV = findViewById(R.id.text_view)

        textBtn.setOnClickListener {
            convertTextToSpeech()
        }

        translateBtn.setOnClickListener {
            val text = "I have a dog."
            translateText(text)
        }
    }

    // Funkcija teksta pārvēršanai uz runu
    private fun convertTextToSpeech() {
        Amplify.Predictions.convertTextToSpeech("I have a dog.",
            {
                textV.text = "Text is converted to speech."
                playAudio(it.audioData)
            }, {
                textV.text = "Text is not converted to speech. There was an error."
            }
        )
    }

    // Funkcija audio faila izveidei
    private fun playAudio(data: InputStream) {
        val mp3File = File(cacheDir, "audio.mp3")
        try {
            FileOutputStream(mp3File).use { out ->
                val buffer = ByteArray(8 * 1024)
                var bytesRead: Int
                while (data.read(buffer).also { bytesRead = it } != -1) {
                    out.write(buffer, 0, bytesRead)
                }
                mp.reset()
                mp.setOnPreparedListener { obj: MediaPlayer -> obj.start() }
                mp.setDataSource(FileInputStream(mp3File).fd)
                mp.prepareAsync()
            }
        } catch (error: IOException) {
            textV.text = "Audio file is not created. There was an error."
        }
    }

    // Funkcija teksta tulkošanai
    private fun translateText(text: String) {
        try {
            Amplify.Predictions.translateText(text,
                { it ->
                    textV.text = "Text is translated: " + it.translatedText
                }, {
                    textV.text = "Text is not translated. There was an error."
                })
        } catch (error: PredictionsException) {
            textV.text = "Text is not translated. There was an error."
        }
    }
}