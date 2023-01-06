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
    var isCurrentSesion: Boolean = false

) : Usuario() {

    override fun toString(): String {
        return super.toString() + "Entrenador(estudios='$estudios', sueldo=$sueldo, fechaAlta=$fechaAlta)"
    }

    companion object {

        fun fromJson(json: String): Entrenador {
            return GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                .create()
                .fromJson(json, Entrenador::class.java)
        }

    }

}