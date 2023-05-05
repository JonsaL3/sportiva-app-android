package es.dao.sportiva.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object Constantes { // ArrebolEnterprise1024

    // Default generic objects
    val DEFAULT_DATE: LocalDateTime = LocalDateTime.of(1900, 1, 1, 0, 0)

    // Animaciones
    const val ANIMACION_DURATION = 200L

    // Llamadas a la api
    private const val IS_DEBUG = true
    private const val BASE_URL_DEBUG = "http://192.168.1.138:8080/api/"
    private const val BASE_URL_RELEASE = "http://arrebol.eu:7290/api/"
    val BASE_URL = if (IS_DEBUG) BASE_URL_DEBUG else BASE_URL_RELEASE

    // Other
    const val TIPO_EMPLEADO = "EMPLEADO"
    const val TIPO_ENTRENADOR = "ENTRENADOR"
    const val SERVER_HEADERS_ERROR_MESSAGE = "errorMessage"
    const val GENERIC_TAG = ":::GenericRepo"
    const val AFORO_ILIMITADO = -1
    const val AFORO_NO_DEFINIDO = -2

}