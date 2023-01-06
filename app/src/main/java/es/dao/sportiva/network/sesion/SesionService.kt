package es.dao.sportiva.network.sesion

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.repository.GenericRepo
import es.dao.sportiva.utils.DxImplementation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SesionService @Inject constructor(private val api: SesionApiClient) {

    suspend fun findSesionesDisponibles(
        idEmpresa: Int,
        context: Context? = null
    ): SesionWrapper? {

        val request = api.findSesionesDisponibles(idEmpresa)
        val response = GenericRepo.genericRequest(request, context)
        var sesionesDisponibles: SesionWrapper? = null

        response?.let { mResponse ->

            if (mResponse is SesionWrapper) {
                sesionesDisponibles = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                    }
                    else -> {}// TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                }

            }

        }

        return sesionesDisponibles
    }

    suspend fun findSesionesDisponiblesByEntrenador(
        idEntrenador: Int,
        context: Context? = null
    ): SesionWrapper? {

        val request = api.findSesionesDisponiblesByEntrenador(idEntrenador)
        val response = GenericRepo.genericRequest(request, context)
        var sesionesDisponiblesDelEntrenador: SesionWrapper? = null

        response?.let { mResponse ->

            if (mResponse is SesionWrapper) {
                sesionesDisponiblesDelEntrenador = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                    }
                    else -> {}// TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                }

            }

        }

        return sesionesDisponiblesDelEntrenador
    }

}