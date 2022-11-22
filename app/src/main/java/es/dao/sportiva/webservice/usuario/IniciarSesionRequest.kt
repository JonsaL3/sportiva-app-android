package es.dao.sportiva.webservice.usuario

import java.io.Serializable

data class IniciarSesionRequest (
    var correo: String = "",
    var contrasena: String = ""
) : Serializable