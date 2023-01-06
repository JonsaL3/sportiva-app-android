package es.dao.sportiva.network.empleado_inscribe_sesion

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import es.dao.sportiva.repository.GenericRepo
import es.dao.sportiva.utils.DxImplementation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmpleadoInscribeSesionService @Inject constructor(private val api: EmpleadoInscribeSesionApiClient) {

    suspend fun findInscripcionesByIdSesion(
        idSesion: Int,
        context: Context? = null
    ): EmpleadoInscribeSesionWrapper? {

        val request = api.findInscripcionesByIdSesion(idSesion)
        val response = GenericRepo.genericRequest(request, context)
        var empleadoInscribeSesion: EmpleadoInscribeSesionWrapper? = null

        response?.let { mResponse ->

            if (mResponse is EmpleadoInscribeSesionWrapper) {
                empleadoInscribeSesion = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                    }
                    else -> {} // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                }

            }

        }

        return empleadoInscribeSesion
    }

}