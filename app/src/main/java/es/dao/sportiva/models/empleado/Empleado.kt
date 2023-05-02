package es.dao.sportiva.models.empleado

import com.google.gson.GsonBuilder
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.utils.LocalDateTimeTypeAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

    fun getStringForQR(): String {
        return super.id.toString() +
                ";" + empresa.id.toString() +
                ";" + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    fun isScannedQrEqualsToThisUserToday(qr: String): Boolean {
        return getStringForQR() == qr
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