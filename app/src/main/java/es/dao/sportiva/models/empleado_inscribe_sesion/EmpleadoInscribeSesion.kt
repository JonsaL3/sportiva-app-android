package es.dao.sportiva.models.empleado_inscribe_sesion

import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.utils.Constantes
import java.io.Serializable
import java.time.LocalDateTime

data class EmpleadoInscribeSesion (

    var id: Int = -1,
    var empleadoInscrito: Empleado = Empleado(),
    var sesionALaQueSeInscribe: Sesion = Sesion(),
    var fechaInscripcion: LocalDateTime = Constantes.DEFAULT_DATE,

    // No me viene del servidor, solo lo utilizamos en la app
    @Transient
    var isConfirmado: Boolean = false

) : Serializable