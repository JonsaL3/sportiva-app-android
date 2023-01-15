package es.dao.sportiva.repository

import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import es.dao.sportiva.network.EmpleadoInscribeSesionApiClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmpleadoInscribeSesionRepo @Inject constructor(
    private var empleadoInscribeSesionApiClient: EmpleadoInscribeSesionApiClient
) : GenericRepo() {

    suspend fun findInscripcionesByIdSesion(idSesion: Int): EmpleadoInscribeSesionWrapper? {
        val request = empleadoInscribeSesionApiClient.findInscripcionesByIdSesion(idSesion)
        return genericRequest(request)
    }
}