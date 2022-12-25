package es.dao.sportiva.models.empleado_inscribe_sesion

import es.dao.sportiva.models.Empleado
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.utils.Constantes
import java.io.Serializable
import java.time.LocalDateTime

data class EmpleadoInscribeSesion (

    var id: Int = -1,
    var empleadoInscrito: Empleado = Empleado(),
    var sesionALaQueSeInscribe: Sesion = Sesion(),
    var fechaInscripcion: LocalDateTime = Constantes.DEFAULT_DATE,

    var isConfirmado: Boolean = false

) : Serializable