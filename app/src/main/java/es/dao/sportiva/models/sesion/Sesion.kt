package es.dao.sportiva.models.sesion

import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.utils.Constantes
import java.io.Serializable
import java.time.LocalDateTime

data class Sesion(

    var id: Int = -1,

    var titulo: String = "",
    var subtitulo: String = "",
    var descripcion: String = "",

    var fechaInserccion: LocalDateTime = Constantes.DEFAULT_DATE,
    var fechaSesion: LocalDateTime = Constantes.DEFAULT_DATE,
    var aforoMaximo: Int = -1,
    var imagen: String? = "",

    var empresa: Empresa? = Empresa(),
    var creador: Entrenador = Entrenador(),
    var entrenadores: MutableList<Entrenador> = mutableListOf()

) : Serializable