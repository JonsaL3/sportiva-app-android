package es.dao.sportiva.models.usuario

import java.io.Serializable

data class IniciarSesionRequest (
    var correo: String = "",
    var contrasena: String = ""
) : Serializable