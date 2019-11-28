package com.example.holapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy: StrictMode.ThreadPolicy =
                StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        signUpBtn.setOnClickListener {
            val intent: Intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        signInBtn.setOnClickListener {
            val intent: Intent = Intent(this, FeedFotos::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun validarCampos(usernaeme: String, password: String): Boolean {
        if (usernaeme.equals("") || password.equals("")) {
            return false
        }

        return true
    }

    fun iniciarSesion() {
        val camposLlenos = validarCampos(userName.text.toString(), password.text.toString())
        if (camposLlenos) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Sesión iniciada")
            builder.setMessage("Se verificarón los campos y SÍ están llenos")
            builder.setPositiveButton("Aceptar", { dialogInterface: DialogInterface, i: Int -> })
            builder.show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Sesión iniciada")
            builder.setMessage("Se verificarón los campos y NO están llenos")
            builder.setPositiveButton("Aceptar", { dialogInterface: DialogInterface, i: Int -> })
            builder.show()
        }
    }
}
