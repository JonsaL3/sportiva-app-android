package es.dao.sportiva.models

import es.dao.sportiva.utils.Constantes
import java.io.Serializable
import java.time.LocalDateTime

abstract class Usuario(

    open var id: Int = -1,
    open var correo: String = "",
    open var contrasena: String = "",
    open var nombre: String = "",
    open var apellido1: String = "",
    open var apellido2: String = "",
    open var fechaNacimiento: LocalDateTime = Constantes.DEFAULT_DATE,
    open var fechaInserccion: LocalDateTime? = Constantes.DEFAULT_DATE,
    open var isActivo: Boolean = false,
    open var imagen: String? = null

) : Serializable {

    override fun toString(): String {
        return "Usuario(id=$id, correo='$correo', contrasena='$contrasena', nombre='$nombre', apellido1='$apellido1', apellido2='$apellido2', fechaNacimiento=$fechaNacimiento, fechaInserccion=$fechaInserccion, isActivo=$isActivo, imagen=$imagen)"
    }

}