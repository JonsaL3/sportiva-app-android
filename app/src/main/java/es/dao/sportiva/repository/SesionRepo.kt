package es.dao.sportiva.repository

import es.dao.sportiva.network.sesion.SesionService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SesionRepo @Inject constructor(private val sesionService: SesionService) {

    suspend fun findSesionesDisponibles(idEmpresa: Int) =
        sesionService.findSesionesDisponibles(idEmpresa)

    suspend fun findSesionesDisponiblesByEntrenador(idEntrenador: Int) =
        sesionService.findSesionesDisponiblesByEntrenador(idEntrenador)

}