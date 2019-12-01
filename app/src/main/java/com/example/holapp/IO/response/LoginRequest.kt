package com.example.holapp.IO.response

class LoginRequest(usuario: String, contrasena: String) {
    private var username: String?
        get() {
            return username
        }
    private var password: String?
        get() {
            return password
        }

    init {
        username = usuario
        password = contrasena
    }
}