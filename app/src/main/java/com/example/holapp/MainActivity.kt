package com.example.holapp

import LoginResponse
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.holapp.IO.response.ApiAdaper
import com.example.holapp.IO.response.LoginRequest
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiAdapter = ApiAdaper().getApiService()
    private val key = "MY_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

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
            val username = userName.text.toString()
            val token = prefs.getString(username, "")
            Log.d("Token: ", token)
            if (camposLlenos()) {
                iniciarSesion(token!!)
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Se necesita usuario y contraseña",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun camposLlenos(): Boolean {
        val username = userName.text.toString()
        val password = password.text.toString()

        if (username.equals("") || password.equals("")) {
            return false
        }

        return true
    }

    fun iniciarSesion(token: String) {
        val username = userName.text.toString()
        val password = password.text.toString()

        Toast.makeText(this@MainActivity, "Iniciando sesión", Toast.LENGTH_SHORT).show()
        val logged = LoginRequest(username, password)
        apiAdapter!!.signIn(token, logged).enqueue(object :
            Callback<LoginResponse> {
            override fun onFailure(call: Call<LoginResponse>?, t: Throwable?) {
                var response = call!!.execute().body().message
                Toast.makeText(
                    this@MainActivity,
                    "onFailure",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<LoginResponse>?,
                response: Response<LoginResponse>?
            ) {
                if (response!!.code() == 200) {
                    Toast.makeText(
                        this@MainActivity,
                        response!!.body().message,
                        Toast.LENGTH_LONG
                    ).show()
                    val intent: Intent = Intent(this@MainActivity, FeedFotos::class.java)
                    startActivity(intent)
                    finish()
                } else if (response.code() == 201) {
                    val prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                    val token = response!!.body().message
                    Toast.makeText(
                        this@MainActivity,
                        response!!.body().message,
                        Toast.LENGTH_LONG
                    ).show()
                    prefs.edit().putString(username, token).apply()
                    val intent: Intent = Intent(this@MainActivity, FeedFotos::class.java)
                    startActivity(intent)
                    finish()
                } else if (response!!.code() == 204) {
                    Toast.makeText(
                        this@MainActivity,
                        "Usuario o contraseña incorrectos",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })
    }
}
