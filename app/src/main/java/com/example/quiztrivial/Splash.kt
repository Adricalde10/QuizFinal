package com.example.quiztrivial

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import kotlin.concurrent.schedule

class Splash : AppCompatActivity() {
    private val duracio: Long = 3000
    private lateinit var mediaPlayer: MediaPlayer // Declaramos el MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Ocultamos la barra de acción
        supportActionBar?.hide()

        // Reproducir el sonido
        mediaPlayer = MediaPlayer.create(this, R.raw.among) // Cambia "tu_sonido" por el nombre del archivo de audio
        mediaPlayer.start() // Inicia la reproducción

        // Llamamos a la función que cambia de actividad después del tiempo de duración
        canviarActivity()
    }

    private fun canviarActivity() {
        Timer().schedule(duracio) {
            saltainici()
        }
    }

    fun saltainici() {
        // Detenemos el sonido si aún se está reproduciendo
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release() // Liberamos recursos
        }

        val intent = Intent(this, LoginRegister::class.java)
        startActivity(intent)
        finish() // Finalizamos la actividad de splash
    }

    override fun onDestroy() {
        super.onDestroy()
        // Asegurarnos de liberar los recursos del MediaPlayer cuando la actividad se destruya
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
}
