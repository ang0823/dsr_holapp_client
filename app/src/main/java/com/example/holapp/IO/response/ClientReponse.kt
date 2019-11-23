package com.example.holapp.IO.response

import com.example.holapp.models.Cliente

class ClientReponse {
    var clientes: ArrayList<Cliente>
        get() {
            return this.clientes
        }
        set(value) {
            this.clientes = value
        }
}