package com.example.quiztrivial

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import android.util.Base64

class Menu : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var user: FirebaseUser? = null
    lateinit var tancarSessio: Button
    lateinit var credits: Button
    lateinit var PuntuacionsBtn: Button
    lateinit var jugarBtn: Button
    lateinit var editarBtn: Button

    lateinit var miPuntuaciotxt: TextView
    lateinit var puntuacio: TextView
    lateinit var uid: TextView
    lateinit var correo: TextView
    lateinit var nom: TextView
    lateinit var edat: TextView
    lateinit var poblacio: TextView

    lateinit var imatgePerfil: ImageView
    private var nivell ="1"
    lateinit var reference: DatabaseReference
    lateinit var imatgeUri: Uri

    private val REQUEST_CODE_GALLERY = 101
    private val REQUEST_CODE_CAMERA = 102
    private val REQUEST_CODE_PERMISSIONS = 201

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        editarBtn = findViewById<Button>(R.id.editarBtn)
        edat=findViewById(R.id.edat)
        PuntuacionsBtn=findViewById(R.id.PuntuacionsBtn)
        poblacio=findViewById(R.id.poblacio)
        imatgePerfil=findViewById(R.id.imageView4)

        // Vinculamos las vistas
        tancarSessio = findViewById(R.id.tancarSessio)
        credits = findViewById(R.id.CreditsBtn)
        jugarBtn = findViewById(R.id.jugarBtn)
        miPuntuaciotxt = findViewById(R.id.miPuntuaciotxt)
        puntuacio = findViewById(R.id.puntuacio)
        uid = findViewById(R.id.uid)
        correo = findViewById(R.id.correo)
        nom = findViewById(R.id.nom)

        // Inicializamos FirebaseAuth y obtenemos el usuario actual
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser
        editarBtn.setOnClickListener(){
            Toast.makeText(this,"EDITAR", Toast.LENGTH_SHORT).show()
        }
        editarBtn.setOnClickListener() {
            Toast.makeText(this, "EDITAR", Toast.LENGTH_SHORT).show()
            canviaLaImatge()
        }
        PuntuacionsBtn.setOnClickListener() {
            Toast.makeText(this, "Puntuacio", Toast.LENGTH_SHORT).show()
            PuntuaciButton()
        }
        // Configuramos el botón para cerrar sesión
        tancarSessio.setOnClickListener {
            tancalaSessio()
        }
        credits.setOnClickListener{
            anarCredits()
        }
        jugarBtn.setOnClickListener{
            var Uids : String = uid.getText().toString()
            var noms : String = nom.getText().toString()
            var puntuacios : String = puntuacio.getText().toString()
            var nivells : String = nivell
            val intent= Intent(this, seleccionivell::class.java)
            Log.d("Pepe", nivells)
            intent.putExtra("UID",Uids)
            intent.putExtra("NOM",noms)
            intent.putExtra("PUNTUACIO",puntuacios)
            intent.putExtra("NIVELL",nivells)
            startActivity(intent)
            finish()
        }
        // Llamamos a consulta para cargar los datos del jugador
        consulta()
    }
    private fun anarCredits() {
        auth.signOut() // Cierra la sesión
        val intent = Intent(this, Credits::class.java)
        startActivity(intent)
        finish()
    }
    private fun PuntuaciButton() {
        val intent = Intent(this, Recyclerview1::class.java)
        startActivity(intent)
        finish()
    }
    private fun tancalaSessio() {
        auth.signOut() // Cierra la sesión
        val intent = Intent(this, LoginRegister::class.java)
        startActivity(intent)
        finish()
    }
    //Método para consultar datos de Firebase
    private fun consulta() {
        val database: FirebaseDatabase =
            FirebaseDatabase.getInstance("https://quiztrivial-8b71c-default-rtdb.europe-west1.firebasedatabase.app/")
        val bdreference: DatabaseReference = database.getReference("DATA BASE JUGADORS")

        bdreference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("DEBUG", "Arrel value: ${snapshot.getValue()}")
                Log.i("DEBUG", "Arrel key: ${snapshot.key}")

                var trobat: Boolean = false
                for (ds in snapshot.children) {
                    Log.i("DEBUG", "DS key: ${ds.child("Uid").key}")
                    Log.i("DEBUG", "DS value: ${ds.child("Uid").getValue()}")
                    Log.i("DEBUG", "DS data: ${ds.child("Data").getValue()}")
                    Log.i("DEBUG", "DS mail: ${ds.child("Email").getValue()}")

                    // Comparamos el email almacenado en la base de datos con el email del usuario actual
                    if (ds.child("Email").getValue().toString() == user?.email) {
                        trobat = true
                        puntuacio.text = ds.child("Puntuacio").getValue().toString()
                        uid.text = ds.child("Uid").getValue().toString()
                        correo.text = ds.child("Email").getValue().toString()
                        nom.text = ds.child("Nom").getValue().toString()
                        nivell = ds.child("Nivell").getValue().toString()
                        poblacio.setText( ds.child("Poblacio").getValue().toString())
                        edat.setText( ds.child("Edat").getValue().toString())
                        var imatge: String = ds.child("Imatge").getValue().toString()
                        val bitmap = ImageUtils.decodeImage(imatge)
                        imatgePerfil.setImageBitmap(bitmap)
                        //Picasso.get().load(imatge).into(imatgePerfil);

                    }
                }

                if (!trobat) {
                    Log.e("ERROR", "ERROR NO TROBAT MAIL")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ERROR", "ERROR DATABASE CANCEL")
            }
        })
    }

    // Verificamos si el usuario está logueado
    private fun usuariLogejat() {
        if (user != null) {
            Toast.makeText(this, "Jugador logejat", Toast.LENGTH_SHORT).show()
        } else {
            val intent = Intent(this, LoginRegister::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Llamamos a usuariLogejat en onStart
    override fun onStart() {
        usuariLogejat()
        super.onStart()
    }
    private fun canviaLaImatge() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("CANVIAR IMATGE")
            .setMessage("Seleccionar imatge de: ")
            .setNegativeButton("Galeria") { _, _ ->
                if (askForPermissions()) {
                    openGallery()
                } else {
                    Toast.makeText(this, "ERROR PERMISOS", Toast.LENGTH_SHORT).show()
                }
            }
            .setPositiveButton("Càmera") { _, _ ->
                if (askForPermissions()) {
                    openCamera()
                } else {
                    Toast.makeText(this, "ERROR PERMISOS", Toast.LENGTH_SHORT).show()
                }
            }
            .setCancelable(false)
            .create()
        dialog.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_GALLERY)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CODE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_GALLERY -> {
                    val selectedImageUri: Uri? = data?.data
                    if (selectedImageUri != null) {
                        imatgePerfil.setImageURI(selectedImageUri)
                        val base64Image = uriToBase64(selectedImageUri)
                        saveToDatabase(base64Image)
                        Toast.makeText(this, "Imagen guardada en Firebase", Toast.LENGTH_LONG).show()
                    }
                }
                REQUEST_CODE_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    if (imageBitmap != null) {
                        imatgePerfil.setImageBitmap(imageBitmap)
                        val base64Image = bitmapToBase64(imageBitmap)
                        saveToDatabase(base64Image)
                        Toast.makeText(this, "Imagen capturada y guardada en Firebase", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun uriToBase64(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val byteArrayOutputStream = ByteArrayOutputStream()
        inputStream?.copyTo(byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun saveToDatabase(base64Image: String) {
        val database = FirebaseDatabase.getInstance("https://quiztrivial-8b71c-default-rtdb.europe-west1.firebasedatabase.app/")
        val ref = database.getReference("DATA BASE JUGADORS").child(uid.getText().toString())

        // Actualizamos el nivel del usuario en la base de datos
        ref.child("Imatge").setValue(base64Image).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("QuizFacil", "Imatge cambiada.")
            } else {
                Log.e("QuizFacil", "Error cambiar imatge.")
            }
        }

    }
    //----------------------------------------Permisos----------------
    fun isPermissionsAllowed(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    fun askForPermissions(): Boolean {
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                Toast.makeText(this, "Permisos concedidos", Toast.LENGTH_SHORT).show()
            } else {
                showPermissionDeniedDialog()
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton("App Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
