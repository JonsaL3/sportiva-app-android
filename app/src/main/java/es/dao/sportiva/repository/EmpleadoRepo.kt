package es.dao.sportiva.repository

import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.network.EmpleadoApiClient
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