package com.example.quiztrivial

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quiztrivial.QuizFacil.Companion
import com.google.firebase.database.FirebaseDatabase

class QuizMitja : AppCompatActivity() {

    companion object {
        // Declaración de las preguntas, opciones y respuestas correctas
        val questions = arrayOf(
            "Quin és el component principal del sol que el fa brillar?",
            "Què és la teoria de la relativitat general d'Albert Einstein?",
            "En quin any es va signar la Declaració d'Independència dels Estats Units?",
            "Què és el principi d'incertesa de Heisenberg?"
        )

        val choices = arrayOf(
            arrayOf("Hel·li", "Oxigen", "Hidrogen","Carboni"),
            arrayOf("La teoria que explica les lleis de la física en espais buits", "La teoria que afirma que la gravetat és una força invisible", "La teoria que afirma que la gravetat és el resultat de la curvatura de l'espai-temps","La teoria que determina que l'espai-temps és finit"),
            arrayOf("1774", "1789", "1776","1790"),
            arrayOf("És un principi que explica com les partícules subatòmiques poden estar en dos llocs alhora", "És un principi que estableix que no es poden mesurar amb precisió la posició i la velocitat d'una partícula simultàniament", "És un principi que defineix la curvatura de l'espai-temps","És un principi que afirma que les partícules sempre tenen un moviment definit")
        )

        val correctAnswers = arrayOf(
            "Hidrogen",
            "La teoria que afirma que la gravetat és el resultat de la curvatura de l'espai-temps",
            "1776",
            "És un principi que estableix que no es poden mesurar amb precisió la posició i la velocitat d'una partícula simultàniament"
        )
    }

    private var currentQuestionIndex = 0
    private var score = 0
    private lateinit var userNameTextView: TextView
    private lateinit var totalQuestionsTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var ansA: Button
    private lateinit var ansB: Button
    private lateinit var ansC: Button
    private lateinit var ansD: Button
    private lateinit var submitButton: Button
    private var userLevel: Int = 2  // Nivel del usuario (inicialmente nivel 1)
    private var NOM: String = ""
    private var PUNTUACIO: String = ""
    private var UID: String = ""
    private var NIVELL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_facil)

        // Inicializar las vistas
        userNameTextView = findViewById(R.id.user_name)
        totalQuestionsTextView = findViewById(R.id.total_question)
        questionTextView = findViewById(R.id.question)
        ansA = findViewById(R.id.ans_A)
        ansB = findViewById(R.id.ans_B)
        ansC = findViewById(R.id.ans_C)
        ansD = findViewById(R.id.ans_D)
        submitButton = findViewById(R.id.submit_btn)

        // Recuperar información de la pantalla anterior (selección de nivel, puntuación, nombre)
        val intent: Bundle? = intent.extras
        if (intent != null) {
            UID = intent.getString("UID", "")
            NOM = intent.getString("NOM", "")
            PUNTUACIO = intent.getString("PUNTUACIO", "")
            NIVELL = intent.getString("NIVELL", "")
        }

        userNameTextView.text = "Bienvenido, $NOM"
        totalQuestionsTextView.text = "Total questions: ${QuizFacil.questions.size}"

        // Inicializa la primera pregunta
        updateQuestion()

        // Configurar los botones de respuesta
        ansA.setOnClickListener { checkAnswer(ansA.text.toString()) }
        ansB.setOnClickListener { checkAnswer(ansB.text.toString()) }
        ansC.setOnClickListener { checkAnswer(ansC.text.toString()) }
        ansD.setOnClickListener { checkAnswer(ansD.text.toString()) }

        // Acción para el botón "Submit"
        submitButton.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex < QuizFacil.questions.size) {
                updateQuestion()  // Mostrar la siguiente pregunta
            } else {
                // Si no hay más preguntas, mostrar el resultado final y actualizar el nivel
                finishQuiz()  // Finalizamos el quiz y actualizamos el nivel
            }
        }
    }

    // Función para actualizar la pregunta y las opciones
    private fun updateQuestion() {
        if (currentQuestionIndex < QuizFacil.questions.size) {
            questionTextView.text = QuizFacil.questions[currentQuestionIndex]
            ansA.text = QuizFacil.choices[currentQuestionIndex][0]
            ansB.text = QuizFacil.choices[currentQuestionIndex][1]
            ansC.text = QuizFacil.choices[currentQuestionIndex][2]
            ansD.text = QuizFacil.choices[currentQuestionIndex].getOrElse(3) { "Opció D" }  // Para evitar posibles nulls
        }
    }

    // Función para verificar si la respuesta seleccionada es correcta
    private fun checkAnswer(selectedAnswer: String) {
        if (currentQuestionIndex >= QuizFacil.questions.size) {
            // Si el quiz ya terminó, no hacer nada o mostrar un mensaje
            Toast.makeText(this, "El quiz ya ha terminado. No puedes responder más.", Toast.LENGTH_SHORT).show()
            return
        }

        val correctAnswer = QuizFacil.correctAnswers[currentQuestionIndex]
        if (selectedAnswer == correctAnswer) {
            score++
        }
        currentQuestionIndex++
        if (currentQuestionIndex < QuizFacil.questions.size) {
            updateQuestion()  // Mostrar la siguiente pregunta
        } else {
            // Si no hay más preguntas, mostrar el resultado final
            questionTextView.text = "Quiz Finished! Your score is $score out of ${QuizFacil.questions.size}"
            submitButton.text = "Finalizar Quiz"  // Cambiar texto del botón a "Finalizar Quiz"
        }
    }

    // Deshabilitar botones de respuesta
    private fun disableAnswerButtons() {
        ansA.isEnabled = false
        ansB.isEnabled = false
        ansC.isEnabled = false
        ansD.isEnabled = false
    }

    // Función que maneja la finalización del quiz y actualización del nivel
    private fun finishQuiz() {
        if (score > 2) {
            // Incrementar el nivel del usuario (o determinar el nuevo nivel)
            userLevel++  // Aumentamos el nivel
        }
        // Llamar a actualizar el nivel en Firebase
        val intentResult = Intent()
        intentResult.putExtra("NIVELL", userLevel.toString())  // Pasamos el nuevo nivel
        setResult(RESULT_OK, intentResult)  // Establecemos el resultado para que la actividad anterior reciba el nuevo nivel

        // Actualizamos el nivel en la base de datos
        if (UID.isNotEmpty()) {
            actualizarNivel(UID, userLevel.toString())  // Actualizamos el nivel del usuario en Firebase
        }

        // Regresar a la actividad anterior
        val backIntent = Intent(this, seleccionivell::class.java)
        backIntent.putExtra("UID", UID)
        backIntent.putExtra("NOM", NOM)
        var puntuaciototal = PUNTUACIO.toInt()+score
        actualitzaPuntuacio(UID, puntuaciototal.toString())
        backIntent.putExtra("PUNTUACIO", puntuaciototal.toString())
        backIntent.putExtra("NIVELL", userLevel.toString())  // Pasamos el nuevo nivel
        startActivity(backIntent)

        // Cerrar esta actividad
        finish()
    }

    // Función para actualizar el nivel en Firebase
    private fun actualizarNivel(uid: String, nivel: String) {
        val database = FirebaseDatabase.getInstance("https://quiztrivial-8b71c-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("DATA BASE JUGADORS").child(uid)

        // Actualizamos el nivel del usuario en la base de datos
        ref.child("Nivell").setValue(nivel).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("QuizFacil", "Nivel actualizado correctamente.")
            } else {
                Log.e("QuizFacil", "Error al actualizar el nivel.")
            }
        }

    }
    private fun actualitzaPuntuacio(uid: String, puntuacio: String) {
        val database = FirebaseDatabase.getInstance("https://quiztrivial-8b71c-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("DATA BASE JUGADORS").child(uid)

        // Actualizamos el nivel del usuario en la base de datos
        ref.child("Puntuacio").setValue(puntuacio).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("QuizFacil", "Nivel actualizado correctamente.")
            } else {
                Log.e("QuizFacil", "Error al actualizar el nivel.")
            }
        }

    }

}
