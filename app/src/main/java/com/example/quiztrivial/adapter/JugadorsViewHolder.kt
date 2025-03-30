package com.example.quiztrivial.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quiztrivial.ImageUtils
import com.example.quiztrivial.Jugador
import com.example.quiztrivial.R

class JugadorsViewHolder (view: View): RecyclerView.ViewHolder(view) {
    //afegim les variables que apunten als continguts del layout
    val nomJugador=view.findViewById<TextView>(R.id.tvNom_Jugador)
    val puntuacioJugador=view.findViewById<TextView>(R.id.tvPuntuacio_Jugador)
    val foto=view.findViewById<ImageView>(R.id.ivJugador)

    fun render(jugadorModel: Jugador){
        //la cridara per a cada jugador
        nomJugador.text=jugadorModel.nom_jugador
        puntuacioJugador.text=jugadorModel.puntuacio.toString() //recordem que és un int
        var imatge: String = jugadorModel.foto
        val bitmap = ImageUtils.decodeImage(imatge)
        foto.setImageBitmap(bitmap)

    }
}