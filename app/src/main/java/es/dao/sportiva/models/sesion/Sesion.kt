package es.dao.sportiva.models.sesion

import com.google.gson.annotations.SerializedName
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.utils.Constantes
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Sesion(

    var id: Int = -1,

    var titulo: String = "",
    var subtitulo: String = "",
    var descripcion: String = "",
    var isLlevadaACabo: Boolean = false,

    var fechaInserccion: LocalDateTime = Constantes.DEFAULT_DATE,
    var fechaSesion: LocalDateTime = Constantes.DEFAULT_DATE,
    var aforoMaximo: Int = -1,
    var imagen: String? = "",

    var empresa: Empresa? = Empresa(),
    var creador: Entrenador = Entrenador(),
    var entrenadores: MutableList<Entrenador> = mutableListOf(),

    // Depende del empleado este campo me viene del servidor.
    @SerializedName("currentEmpleadoInscrito")
    var isCurrentEmpleadoInscrito: Boolean = false,

) : Serializable {

    constructor(
        titulo: String,
        subtitulo: String,
        descripcion: String,
        isLlevadaACabo: Boolean,
        fechaInserccion: LocalDateTime,
        fechaSesion: LocalDateTime,
        aforoMaximo: Int,
        imagen: String?,
        empresa: Empresa?,
        creador: Entrenador
    ) : this(
        -1,
        titulo,
        subtitulo,
        descripcion,
        isLlevadaACabo,
        fechaInserccion,
        fechaSesion,
        aforoMaximo,
        imagen,
        empresa,
        creador,
        mutableListOf()
    )

    fun getFechaString(): String {
        return fechaSesion.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun getHoraString(): String {
        return fechaSesion.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun isLaUnaYAlgo(): Boolean {
        return getHoraString().contains("01:")
    }

    fun getFechaHoraFancyString(): String {
        return getFechaString() + if (isLaUnaYAlgo()) "a la " else " a las " + getHoraString()
    }

}