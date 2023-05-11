package es.dao.sportiva.models.empleado_participa_sesion

import es.dao.sportiva.models.empleado.EmpleadoWrapper
import es.dao.sportiva.models.sesion.Sesion

data class ComenzarSesionRequest(
    val sesion: Sesion,
    val participaciones: EmpleadoWrapper
)