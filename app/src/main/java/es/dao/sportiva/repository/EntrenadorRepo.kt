package es.dao.sportiva.repository

import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.network.EntrenadorApiClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntrenadorRepo @Inject constructor(
    private val entrenadorApiClient: EntrenadorApiClient
) : GenericRepo() {

    suspend fun findEntrenadoresByIdEmpresa(idEmpresa: Int): EntrenadorWrapper? {
        val request = entrenadorApiClient.findEntrenadoresByIdEmpresa(idEmpresa)
        return genericRequest(request)
    }

    suspend fun registerEntrenador(entrenador: Entrenador): Boolean? {
        val request = entrenadorApiClient.registrarEntrenador(entrenador)
        return genericRequest(request)
    }

}