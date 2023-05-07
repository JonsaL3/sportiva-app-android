package es.dao.sportiva.models

import com.google.gson.GsonBuilder
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.utils.LocalDateTimeTypeAdapter
import java.time.LocalDateTime

data class Empleado(
    var cargo: String = "",
    var peso: Float? = -1.0f,
    var altura: Float? = -1.0f,
    var isDeporteFrecuente: Boolean? = false,
    var isFumador: Boolean? = false,
    var empresa: Empresa = Empresa(),
) : Usuario() {

    constructor(
        id: Int,
        correo: String,
        contrasena: String,
        nombre: String,
        apellido1: String,
        apellido2: String,
        fechaNacimiento: LocalDateTime,
        fechaInserccion: LocalDateTime?,
        isActivo: Boolean,
        imagen: String?,
        cargo: String,
        peso: Float?,
        altura: Float?,
        isDeporteFrecuente: Boolean?,
        isFumador: Boolean?,
        empresa: Empresa
    ) : this(cargo, peso, altura, isDeporteFrecuente, isFumador) {
        this.id = id
        this.correo = correo
        this.contrasena = contrasena
        this.nombre = nombre
        this.apellido1 = apellido1
        this.apellido2 = apellido2
        this.fechaNacimiento = fechaNacimiento
        this.fechaInserccion = fechaInserccion
        this.isActivo = isActivo
        this.imagen = imagen
        this.empresa = empresa
    }

    override fun toString(): String {
        return super.toString() + "Empleado(cargo='$cargo', peso=$peso, altura=$altura, isDeporteFrecuente=$isDeporteFrecuente, isFumador=$isFumador, empresa=$empresa)"
    }

    companion object {

        fun fromJson(json: String): Empleado {
            return GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()
                .fromJson(json, Empleado::class.java)
        }

    }

}