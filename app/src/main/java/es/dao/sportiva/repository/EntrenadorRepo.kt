package es.dao.sportiva.repository

import es.dao.sportiva.network.entrenador.EntrenadorService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntrenadorRepo @Inject constructor(private val service: EntrenadorService) {

    suspend fun findEntrenadoresByIdEmpresa(idEmpresa: Int) =
        service.findEntrenadoresByIdEmpresa(idEmpresa)

}