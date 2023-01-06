package es.dao.sportiva.models.usuario

import es.dao.sportiva.models.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.utils.Constantes
import java.io.Serializable

data class IniciarSesionResponse(
    var tipo: String = "",
    var json: String = ""
) : Serializable {

    fun getInstance(): Usuario? {

        return when (tipo) {
            Constantes.TIPO_EMPLEADO -> {
                Empleado.fromJson(json)
            }
            Constantes.TIPO_ENTRENADOR -> {
                Entrenador.fromJson(json)
            }
            else -> {
                null
            }
        }

    }

}
