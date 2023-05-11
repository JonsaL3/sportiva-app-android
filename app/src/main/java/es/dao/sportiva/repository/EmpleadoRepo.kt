package es.dao.sportiva.repository

import es.dao.sportiva.models.Empleado
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.network.EmpleadoApiClient
import es.dao.sportiva.network.UsuarioApiClient
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmpleadoRepo @Inject constructor(
    private val empleadpApiClient: EmpleadoApiClient,
) : GenericRepo() {

    suspend fun registerEmpleado(empleado: Empleado): Boolean? {
        val request = empleadpApiClient.registrarEmpleado(empleado)
        return genericRequest(request)
    }

}