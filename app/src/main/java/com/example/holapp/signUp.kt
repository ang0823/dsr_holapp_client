package com.example.holapp

import SignUpResponse
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.holapp.IO.response.ApiAdaper
import com.example.holapp.IO.response.ClientReponse
import com.example.holapp.models.Cliente
import kotlinx.android.synthetic.main.activity_sign_up.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class signUp : AppCompatActivity() {

    private val apiAdapter = ApiAdaper().getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)



        cancelarBtn.setOnClickListener {
            val intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        finalizarBtn.setOnClickListener {
            guardarEnBD()
        }
    }

    fun validarCampos(fields: ArrayList<String>): Boolean {
        print("Validando campos")
        for (field in fields) {
            if (field.equals("")) {
                return false
            }
        }

        return true
    }

    fun validarComtrasenas(): Boolean {
        print("Validadndo contraseñas")
        var password = passwordField.text.toString()
        var repassword = repasswordField.text.toString()

        if (!password.equals(repassword)) {
            return false
        }

        return true;
    }

    fun esNuevoUsuario(): Boolean {
        var username = usernameField.text.toString()
        var registered = false
        var title: String
        var content: String

        try {
            val client: Call<List<Cliente>> =
                apiAdapter!!.getClientByUsername(username)

            client.enqueue(object : Callback<List<Cliente>> {
                override fun onFailure(call: Call<List<Cliente>>?, t: Throwable?): Boolean {
                    title = "Error: "
                    content = "No se pudo conectar a API"
                    makeAlertDialog(title, content)
                    return registered
                }

                override fun onResponse(
                    call: Call<List<Cliente>>?,
                    response: Response<List<Cliente>>?
                ) {
                    if (!response!!.isSuccessful()) {
                        title = "Disponible"
                        content = "El usuario está disponible"
                        makeAlertDialog(title, content)
                    }

                    var clientsList = response.body()
                    for (client: Cliente in clientsList) {
                        registered = client.success
                    }
                }
            })
        } catch (error: TypeNotPresentException) {
            title = "Error de llamada"
            content = "Ocurrio un error al acceder al API"
            makeAlertDialog(title, content)
        }
        return registered
    }

    fun getInputData(): ArrayList<String> {
        print("Obteniendo la entrada del usuario para empezar a trabajar")
        val fields: ArrayList<String> = ArrayList()

        var name = nombreField.text.toString()
        var apellidoP = paternoField.text.toString()
        var apellidoM = maternoField.text.toString()
        var user = usernameField.text.toString()
        var password = passwordField.text.toString()
        var repassword = repasswordField.text.toString()

        fields.add(name)
        fields.add(apellidoP)
        fields.add(apellidoM)
        fields.add(user)
        fields.add(password)
        fields.add(repassword)

        return fields
    }

    fun guardarEnBD(): Boolean {
        var camposLlenos = validarCampos(getInputData())

        if (camposLlenos) {
            print("Los campos están llenos")
            if (validarComtrasenas()) {
                print("Las contraseñas son iguales")
                if (esNuevoUsuario()) {
                    try {
                        print("Se entro al try/catch")
                        apiAdapter!!.createClient (
                            nombreField.text.toString(),
                            paternoField.text.toString(),
                            maternoField.text.toString(),
                            usernameField.text.toString(),
                            repasswordField.text.toString()
                        )
                        makeAlertDialog("Usuario creado", "El nuevo cliente ya está disponible en la base de datos")
                    } catch (error: TypeNotPresentException) {
                        passwordField.setText("")
                        repasswordField.setText("")
                        makeAlertDialog("Exception", "Error al conectar con API")
                    }
                } else {
                    makeAlertDialog("No disponible", "El usuario introducido ya exise")
                }
            } else {
                val builder = AlertDialog.Builder(this)
                passwordField.setText("")
                repasswordField.setText("")
                builder.setTitle("Repetir contraseñas")
                builder.setMessage("Las contraseñas no coinciden")
                builder.setPositiveButton(
                    "Aceptar",
                    { dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }
        } else {
            makeAlertDialog(
                "Campos Vacíos",
                "Todos los campos son necesarios para continuar. Por favor, inténtalo de nuevo."
            )
        }

        return true
    }

    fun makeAlertDialog(title: String, content: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(content)
        builder.setPositiveButton("Aceptar", { dialogInterface: DialogInterface, i: Int -> })
        builder.show()
    }
}
