package com.example.quiztrivial

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztrivial.adapter.JugadorsAdapter
import com.google.firebase.database.*

class Recyclerview1 : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JugadorsAdapter
    private val jugadorsList = mutableListOf<Jugador>()

    private val database: FirebaseDatabase =
        FirebaseDatabase.getInstance("https://quiztrivial-8b71c-default-rtdb.europe-west1.firebasedatabase.app")
    private val bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recyclerview1)

        recyclerView = findViewById(R.id.RecyclerOne)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = JugadorsAdapter(jugadorsList)
        recyclerView.adapter = adapter

        loadJugadorsFromFirebase()
    }

    private fun loadJugadorsFromFirebase() {
        bdreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                jugadorsList.clear()
                Log.d("FirebaseData", "Datos recibidos: ${snapshot.value}") // Verifica datos en Logcat
                for (data in snapshot.children) {
                    val nom = data.child("Nom").getValue(String::class.java) ?: ""
                    val punts = data.child("Puntuacio").getValue(String::class.java)?.toIntOrNull() ?: 0
                    val imatgeBase64 = data.child("Imatge").getValue(String::class.java) ?: ""
                    jugadorsList.add(Jugador(nom, punts, imatgeBase64))
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error obteniendo datos", error.toException())
            }
        })
    }
}
