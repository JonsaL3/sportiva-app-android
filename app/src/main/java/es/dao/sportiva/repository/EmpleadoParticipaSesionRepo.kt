package es.dao.sportiva.repository

import es.dao.sportiva.models.empleado_participa_sesion.ComenzarSesionRequest
import es.dao.sportiva.models.empleado_participa_sesion.EmpleadoParticipaSesionWrapper
import es.dao.sportiva.network.EmpleadoParticipaSesionApiClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmpleadoParticipaSesionRepo @Inject constructor(
    private var empleadoParticipaSesionApiClient: EmpleadoParticipaSesionApiClient
) : GenericRepo() {

    suspend fun comenzarSesion(eps: ComenzarSesionRequest): EmpleadoParticipaSesionWrapper? {
        val request = empleadoParticipaSesionApiClient.comenzarSesion(eps)
        return genericRequest(request)
    }
}