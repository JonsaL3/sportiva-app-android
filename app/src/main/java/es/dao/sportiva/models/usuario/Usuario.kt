package es.dao.sportiva.models.usuario

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Usuario) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

    fun inicialOrGuion(): String {
        return if (nombre.isNotEmpty()) {
            nombre[0].toString()
        } else {
            "-"
        }
    }

}