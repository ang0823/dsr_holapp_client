package com.example.holapp

import SignUpResponse
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.holapp.IO.response.ApiAdaper
import com.example.holapp.models.Cliente
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SignUp : AppCompatActivity() {

    private val apiAdapter = ApiAdaper().getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        cancelarBtn.setOnClickListener {
            val fields = getInputData()
            if (validarCampos(fields)){
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Cancelar Registro")
                builder.setMessage("¿Seguro que quieres descartar los cambios?")
                builder.setPositiveButton("Aceptar", { dialogInterface: DialogInterface, i: Int ->
                    finish()
                })
                builder.setNegativeButton("Cancelar", { dialogInterface: DialogInterface, i: Int ->})
                builder.show()
            } else {
                finish()
            }
        }

        finalizarBtn.setOnClickListener {
            guardarEnBD()
        }
    }

    fun validarCampos(fields: ArrayList<String>): Boolean {

        for (field in fields) {
            if (field.equals("")) {
                return false
            }
        }

        return true
    }

    fun validarComtrasenas(): Boolean {
        var password = passwordField.text.toString()
        var repassword = repasswordField.text.toString()

        if (password.length < 8 && repassword.length < 8) {
            Toast.makeText(
                this@SignUp,
                "La contraseña debe tener mínimo 8 caracteres",
                Toast.LENGTH_LONG
            )
                .show()

            return false
        }

        if (!password.equals(repassword)) {
            passwordField.setText("")
            repasswordField.setText("")

            Toast.makeText(
                this@SignUp,
                "Las contraseñas no son iguales",
                Toast.LENGTH_LONG
            )
                .show()

            return false
        }

        if (password.length > 30 && repassword.length > 30) {
            Toast.makeText(
                this@SignUp,
                "La contraseña es demasiado larga",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        return true
    }

    fun esNuevoUsuario(): Boolean {
        var username = usernameField.text.toString()
        var registered: Boolean = true
        try {
            val client: Call<List<Cliente>> =
                apiAdapter!!.getClientByUsername(username)

            var res = client.execute()
            if (res.isSuccessful && (res.body().get(0).registered == false))
                registered = false
            else if (res.isSuccessful && (res.body().get(0).registered == true)) {
                usernameField.requestFocus()
                registered = true
            }
        } catch (error: IOException) {
            Log.d("Exception", error.toString())
        } catch (error: TypeNotPresentException) {
            makeAlertDialog("Falla del sistema", "No se pudo guardar la información")
        }
        return registered
    }

    fun getInputData(): ArrayList<String> {
        val fields: ArrayList<String> = ArrayList()

        var name = nombreField.text.toString()
        fields.add(name)
        var apellidoP = paternoField.text.toString()
        fields.add(apellidoP)
        var apellidoM = maternoField.text.toString()
        fields.add(apellidoM)
        var user = usernameField.text.toString()
        fields.add(user)
        var password = passwordField.text.toString()
        fields.add(password)
        var repassword = repasswordField.text.toString()
        fields.add(repassword)

        return fields
    }

    fun guardarEnBD() {
        var camposLlenos = validarCampos(getInputData())

        if (camposLlenos) {

            if (validarComtrasenas()) {
                var nuevo: Boolean = false
                Thread {
                    Runnable {
                        nuevo = esNuevoUsuario()
                    }
                }.start()
                if (!nuevo) {
                    Toast.makeText(this@SignUp, "Iniciando registro...", Toast.LENGTH_SHORT).show()
                    try {
                        var nuevo = Cliente(
                            nombreField.text.toString(),
                            paternoField.text.toString(),
                            maternoField.text.toString(),
                            usernameField.text.toString(),
                            passwordField.text.toString()
                        )

                        apiAdapter!!.createClient(nuevo).enqueue(object : Callback<SignUpResponse> {
                            override fun onFailure(call: Call<SignUpResponse>?, t: Throwable?) {
                                Toast.makeText(
                                    this@SignUp,
                                    "Error de conexión con el servidor",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            override fun onResponse(
                                call: Call<SignUpResponse>?,
                                response: Response<SignUpResponse>?
                            ) {
                                if (response!!.isSuccessful && response!!.code() == 200) {
                                    Toast.makeText(
                                        this@SignUp,
                                        "Usuario guardado con éxito, ya puedes iniciar sesión",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    val token = response.body().token
                                    val key = usernameField.text.toString()
                                    finish()
                                } else if (response!!.code() == 204) {
                                    Toast.makeText(
                                        this@SignUp,
                                        "El nombre de usuario ya se encuentra en uso",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        this@SignUp,
                                        "Error 500. No se pudo guardar usuario.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                    } catch (error: TypeNotPresentException) {
                        makeAlertDialog("Exception", "No se pudo crear el usuario")
                    }
                }
            }
        } else {
            Toast.makeText(
                this@SignUp,
                "Faltan campos por llenar",
                Toast.LENGTH_LONG
            )
                .show()
        }
    }

    fun makeAlertDialog(title: String, content: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setPositiveButton("Aceptar", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }
}
