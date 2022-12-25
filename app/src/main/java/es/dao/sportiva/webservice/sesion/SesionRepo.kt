package es.dao.sportiva.webservice.sesion

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.webservice.GenericRepo
import es.dao.sportiva.webservice.RetrofitSingleton

object SesionRepo {

    suspend fun findSesionesDisponibles(
        idEmpresa: Int,
        context: Context? = null
    ): SesionWrapper? {

        val request = RetrofitSingleton.getRetrofitInstance()?.create(SesionService::class.java)?.findSesionesDisponibles(idEmpresa)
        val response = GenericRepo.genericRequest(request, context)
        var sesionesDisponibles: SesionWrapper? = null

        response?.let { mResponse ->

            if (mResponse is SesionWrapper) {
                sesionesDisponibles = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        DxImplementation.mostrarDxWarning(
                            context = it,
                            mensaje = context.getString(R.string.no_se_han_encontrado_sesiones)
                        )
                    }
                    else -> Log.e(":::FIND SESIONES DISPONIBLES SESION REPO", "Se ha producido un error desconocido")
                }

            }

        }

        return sesionesDisponibles
    }

    suspend fun findSesionesDisponiblesByEntrenador(
        idEntrenador: Int,
        context: Context? = null
    ): SesionWrapper? {

        val request = RetrofitSingleton.getRetrofitInstance()?.create(SesionService::class.java)?.findSesionesDisponiblesByEntrenador(idEntrenador)
        val response = GenericRepo.genericRequest(request, context)
        var sesionesDisponiblesDelEntrenador: SesionWrapper? = null

        response?.let { mResponse ->

            if (mResponse is SesionWrapper) {
                sesionesDisponiblesDelEntrenador = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        DxImplementation.mostrarDxWarning(
                            context = it,
                            mensaje = "No se han encontrado sesiones disponibles activas para "
                        )
                    }
                    else -> Log.e(":::FIND SESIONES DISPONIBLES SESION REPO", "Se ha producido un error desconocido")
                }

            }

        }

        return sesionesDisponiblesDelEntrenador
    }


}