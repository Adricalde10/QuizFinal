package com.example.quiztrivial

import android.content.Intent
import android.widget.Button
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import java.util.Timer
import java.util.TimerTask

class Credits : AppCompatActivity() {
    private var timer = Timer()
    lateinit var buttonEnrere: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_credits)

        // Inicia el temporizador
        timer.scheduleAtFixedRate(TimeTask(), 0L, 3000L)

        buttonEnrere = findViewById(R.id.buttonEnrere)  // Inicializamos correctamente el botón

        // Configura el botón para volver al menú
        buttonEnrere.setOnClickListener {
           boton()
        }
    }
    fun boton() {
        intent = Intent(this, Menu::class.java)
        startActivity(intent)
        finish()
    }
    // Clase interna TimeTask para cambiar los fragments
    private inner class TimeTask : TimerTask() {
        private var numeroFragment: Int = 1

        override fun run() {
            numeroFragment++
            if (numeroFragment > 2) numeroFragment = 1

            runOnUiThread {  // Ejecutamos operaciones en el hilo principal
                if (numeroFragment == 1) {
                    // Cambiar al fragment_primer
                    supportFragmentManager.commit {
                        replace<fragment_primer>(R.id.frameContainer)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                } else {
                    // Cambiar al fragment_segon
                    supportFragmentManager.commit {
                        replace<fragment_segon>(R.id.frameContainer)
                        setReorderingAllowed(true)
                        addToBackStack("replacement")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()  // Detener el temporizador cuando la actividad se destruye
    }
}
