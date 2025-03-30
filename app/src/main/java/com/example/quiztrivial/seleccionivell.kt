package com.example.quiztrivial

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class seleccionivell : AppCompatActivity() {

    private var NOM: String = ""
    private var PUNTUACIO: String = ""
    private var UID: String = ""
    private var NIVELL: String = ""
    lateinit var imageButton1: ImageButton
    lateinit var imageButton2: ImageButton
    lateinit var imageButton3: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccionivell)

        // Recuperar valores de Intent de manera segura
        val intent: Bundle? = intent.extras
        UID = intent?.getString("UID", "") ?: ""
        NOM = intent?.getString("NOM", "") ?: ""
        PUNTUACIO = intent?.getString("PUNTUACIO", "") ?: ""
        NIVELL = intent?.getString("NIVELL", "") ?: ""

        // Buscar los botones
        imageButton1 = findViewById(R.id.imageButton)
        imageButton2 = findViewById(R.id.imageButton2)
        imageButton3 = findViewById(R.id.imageButton3)

        // Deshabilitar los botones inicialmente
        imageButton1.isEnabled = false
        imageButton2.isEnabled = false
        imageButton3.isEnabled = false
        imageButton1.visibility = View.GONE
        imageButton2.visibility = View.GONE
        imageButton3.visibility = View.GONE

        // Detectar y mostrar el nivel adecuado
        when (NIVELL) {
            "1" -> {
                Toast.makeText(this, "NIVELL 1", Toast.LENGTH_LONG).show()
                imageButton1.isEnabled = true
                imageButton1.visibility = View.VISIBLE

                // Al hacer clic en el botón del nivel 1, cambia de actividad
                imageButton1.setOnClickListener {
                    val intentNivel1 = Intent(this, QuizFacil::class.java)
                    intentNivel1.putExtra("UID", UID)
                    intentNivel1.putExtra("NOM", NOM)
                    intentNivel1.putExtra("PUNTUACIO", PUNTUACIO)
                    intentNivel1.putExtra("NIVELL", NIVELL)
                    startActivity(intentNivel1)
                    finish()
                }
            }
            "2" -> {
                Toast.makeText(this, "NIVELL 2", Toast.LENGTH_LONG).show()
                imageButton2.isEnabled = true
                imageButton2.visibility = View.VISIBLE

                // Al hacer clic en el botón del nivel 2, cambia de actividad
                imageButton2.setOnClickListener {
                    val intentNivel2 = Intent(this, QuizMitja::class.java)
                    intentNivel2.putExtra("UID", UID)
                    intentNivel2.putExtra("NOM", NOM)
                    intentNivel2.putExtra("PUNTUACIO", PUNTUACIO)
                    intentNivel2.putExtra("NIVELL", "2")  // Se asegura de pasar el nivel correcto
                    startActivity(intentNivel2)
                    finish()
                }
            }
            "3" -> {
                Toast.makeText(this, "NIVELL 3", Toast.LENGTH_LONG).show()
                imageButton3.isEnabled = true
                imageButton3.visibility = View.VISIBLE

                // Al hacer clic en el botón del nivel 3, cambia de actividad
                imageButton3.setOnClickListener {
                    val intentNivel3 = Intent(this, QuizDificil::class.java)
                    intentNivel3.putExtra("UID", UID)
                    intentNivel3.putExtra("NOM", NOM)
                    intentNivel3.putExtra("PUNTUACIO", PUNTUACIO)
                    intentNivel3.putExtra("NIVELL", "3")  // Se asegura de pasar el nivel correcto
                    startActivity(intentNivel3)
                    finish()
                }
            }
            else -> {
                // Si el nivel no es reconocido, mostrar un mensaje
                Toast.makeText(this, "Nivel no disponible", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
