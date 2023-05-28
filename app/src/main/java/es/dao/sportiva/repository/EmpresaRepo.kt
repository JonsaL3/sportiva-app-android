package es.dao.sportiva.repository

import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.RegistroRequest
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.network.EmpresaApiClient
import es.dao.sportiva.network.UsuarioApiClient
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmpresaRepo @Inject constructor(
    private val empresaApiClient: EmpresaApiClient,
) : GenericRepo() {

    suspend fun findAll(): List<Empresa>?{
        val request = empresaApiClient.findAll()
        return genericRequest(request)
    }

}