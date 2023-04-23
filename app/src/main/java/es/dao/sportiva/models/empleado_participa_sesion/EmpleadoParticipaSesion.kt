package es.dao.sportiva.models.empleado_participa_sesion

import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.utils.Constantes
import java.io.Serializable
import java.time.LocalDateTime

data class EmpleadoParticipaSesion (
    var id: Int = -1,
    var empleadoParticipante: Empleado = Empleado(),
    var sesionEnLaQueParticipa: Sesion = Sesion(),
    var fechaParticipacion: LocalDateTime = Constantes.DEFAULT_DATE,
) : Serializable