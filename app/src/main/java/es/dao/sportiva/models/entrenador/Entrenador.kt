package es.dao.sportiva.models.entrenador

import com.google.gson.GsonBuilder
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.utils.LocalDateTimeTypeAdapter
import java.time.LocalDateTime

data class Entrenador(
    var estudios: String = "",
    var sueldo: Float = -1.0f,
    var fechaAlta: LocalDateTime = Constantes.DEFAULT_DATE,
    var empresaAsignada: Empresa = Empresa(),

    // No me vienen, solo los uso internamente.
    var isCurrentSesion: Boolean = false,
    var isSeleccionadoParaSerParticipante: Boolean = false

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
        estudios: String,
        sueldo: Float,
        fechaAlta: LocalDateTime,
        empresaAsignada: Empresa,
        isCurrentSesion: Boolean,
        isSeleccionadoParaSerParticipante: Boolean
    ) : this(estudios, sueldo, fechaAlta, empresaAsignada, isCurrentSesion, isSeleccionadoParaSerParticipante) {
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
    }

    override fun toString(): String {
        return super.toString() + "Entrenador(estudios='$estudios', sueldo=$sueldo, fechaAlta=$fechaAlta)"
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    fun copy(): Entrenador = Entrenador(
        this.id,
        this.correo,
        this.contrasena,
        this.nombre,
        this.apellido1,
        this.apellido2,
        this.fechaNacimiento,
        this.fechaInserccion,
        this.isActivo,
        this.imagen,
        this.estudios,
        this.sueldo,
        this.fechaAlta,
        this.empresaAsignada,
        this.isCurrentSesion,
        this.isSeleccionadoParaSerParticipante
    )

    companion object {

        fun fromJson(json: String): Entrenador {
            return GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()
                .fromJson(json, Entrenador::class.java)
        }

    }

}