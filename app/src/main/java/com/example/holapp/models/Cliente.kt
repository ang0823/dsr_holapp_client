package com.example.holapp.models

class Cliente(var name: String, var paterno: String, var materno: String, var usuario: String, var contrasena: String) {
    var registered: Boolean = false
        get() {
            return registered
        }

    private var nombre: String?
        get() {
            return nombre
        }

    var apellido_p: String?
        get() {
            return apellido_p
        }

    var apellido_m: String?
        get() {
            return apellido_m
        }

    var username: String?
        get() {
            return username
        }

    var password: String?
        get() {
            return password
        }

    init {
        nombre = name
        apellido_p = paterno
        apellido_m = materno
        username = usuario
        password = contrasena
    }
}