package com.example.quiztrivial

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class QuizDificil : AppCompatActivity() {

    companion object {
        // Declaración de las preguntas, opciones y respuestas correctas
        val questions = arrayOf(
            "Quina és la causa principal del canvi climàtic actual segons els científics?",
            "Quina és la teoria de la matèria i l'energia que va proposar Albert Einstein en 1915?",
            "Què descriu la llei de Hardy-Weinberg en genètica?",
            "Quina és la funció de la mitocòndria en les cèl·lules eucariotes?"
        )

        val choices = arrayOf(
            arrayOf("Augment de l'activitat solar", "Desforestació massiva", " Activitat humana, especialment les emissions de gasos d'efecte hivernacle","Variacions naturals en les emissions de carboni"),
            arrayOf("Teoria de la relativitat especial", "Teoria del big bang", "Teoria de la relativitat general","Teoria quàntica"),
            arrayOf("Com es produeix la mutació genètica", "Com la selecció natural afavoreix certs trets", "L'equilibri de les freqüències al·lèliques en una població ideal","La recombinació genètica durant la meiosi"),
            arrayOf("Sintetitzar proteïnes", "Produeix energia a través de la respiració cel·lular", "Emmagatzemar informació genètica","Controlar la divisió cel·lular")
        )

        val correctAnswers = arrayOf(
            "Activitat humana, especialment les emissions de gasos d'efecte hivernacle",
            "Teoria de la relativitat general",
            "L'equilibri de les freqüències al·lèliques en una població ideal",
            "Produeix energia a través de la respiració cel·lular"
        )
    }

    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_facil)

        val questionTextView: TextView = findViewById(R.id.question)
        val totalQuestionsTextView: TextView = findViewById(R.id.total_question)
        val ansA: Button = findViewById(R.id.ans_A)
        val ansB: Button = findViewById(R.id.ans_B)
        val ansC: Button = findViewById(R.id.ans_C)
        val ansD: Button = findViewById(R.id.ans_D)
        val submitButton: Button = findViewById(R.id.submit_btn)

        // Mostrar el total de preguntas
        totalQuestionsTextView.text = "Total questions: ${questions.size}"

        // Función para actualizar la pregunta y las opciones
        fun updateQuestion() {
            if (currentQuestionIndex < questions.size) {
                questionTextView.text = questions[currentQuestionIndex]
                ansA.text = choices[currentQuestionIndex][0]
                ansB.text = choices[currentQuestionIndex][1]
                ansC.text = choices[currentQuestionIndex][2]
                ansD.text = choices[currentQuestionIndex][3]  // Para evitar posibles nulls
            }
        }

        // Función para verificar si la respuesta seleccionada es correcta
        fun checkAnswer(selectedAnswer: String) {
            val correctAnswer = correctAnswers[currentQuestionIndex]
            if (selectedAnswer == correctAnswer) {
                score++
            }
        }

        // Configurar los botones de respuesta
        ansA.setOnClickListener { checkAnswer(ansA.text.toString()) }
        ansB.setOnClickListener { checkAnswer(ansB.text.toString()) }
        ansC.setOnClickListener { checkAnswer(ansC.text.toString()) }
        ansD.setOnClickListener { checkAnswer(ansD.text.toString()) }

        // Acción para el botón "Submit"
        submitButton.setOnClickListener {
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                updateQuestion()  // Mostrar la siguiente pregunta
            } else {
                // Si no hay más preguntas, mostrar el resultado final
                questionTextView.text = "Quiz Finished! Your score is $score out of ${questions.size}"
                submitButton.visibility = View.INVISIBLE  // Ocultar el botón de submit
            }
        }

        // Inicializa la primera pregunta
        updateQuestion()
    }
}