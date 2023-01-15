package es.dao.sportiva.repository

import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.network.SesionApiClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SesionRepo @Inject constructor(
    private val sesionApiClient: SesionApiClient
) : GenericRepo() {

    suspend fun findSesionesDisponibles(idEmpresa: Int): SesionWrapper? {
        val request = sesionApiClient.findSesionesDisponibles(idEmpresa)
        return genericRequest(request)
    }

    suspend fun findSesionesDisponiblesByEntrenador(idEntrenador: Int): SesionWrapper? {
        val request = sesionApiClient.findSesionesDisponiblesByEntrenador(idEntrenador)
        return genericRequest(request)
    }

    suspend fun crearSesion(sesion: Sesion): Sesion? {
        val request = sesionApiClient.crearSesion(sesion)
        return genericRequest(request)
    }

}