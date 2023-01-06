package es.dao.sportiva.repository

import es.dao.sportiva.network.empleado_inscribe_sesion.EmpleadoInscribeSesionService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmpleadoInscribeSesionRepo @Inject constructor(private var EmpleadoInscribeSesionService: EmpleadoInscribeSesionService) {

    suspend fun findInscripcionesByIdSesion(idSesion: Int) =
        EmpleadoInscribeSesionService.findInscripcionesByIdSesion(idSesion)
}