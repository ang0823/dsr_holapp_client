package com.example.holapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog

class FeedFotos : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_fotos)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Estás seguro que deseas salir?")
        builder.setCancelable(true)
        builder.setNegativeButton("No", DialogInterface.OnClickListener {dialogInterface, i ->
            dialogInterface.cancel()
        })

        builder.setPositiveButton("Salir", DialogInterface.OnClickListener {dialogInterface, i ->
            finish()
        })
        builder.show()
    }

    // Inicializa los items del menu en ActionBar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_activity_feed_fotos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Se asigna funcionamiento a cada item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_perfil -> {
                val intent: Intent = Intent(this, Profile::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_mensaje -> {
                val intent: Intent = Intent(this, Mensajes::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_buscar -> {
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
