package es.dao.sportiva.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object Constantes {

    // Default generic objects
    val DEFAULT_DATE = LocalDateTime.of(1900, 1, 1, 0, 0)

    // Llamadas a la api
    private val IS_DEBUG = true
    private val BASE_URL_DEBUG = "http://192.168.1.142:8080/api/"
    private val BASE_URL_RELEASE = "http://indytek.es/api/"
    val BASE_URL = if (IS_DEBUG) BASE_URL_DEBUG else BASE_URL_RELEASE

    // Other
    val TIPO_EMPLEADO = "EMPLEADO"
    val TIPO_ENTRENADOR = "ENTRENADOR"

}