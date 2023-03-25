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

    override fun toString(): String {
        return super.toString() + "Empleado(cargo='$cargo', peso=$peso, altura=$altura, isDeporteFrecuente=$isDeporteFrecuente, isFumador=$isFumador, empresa=$empresa)"
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
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