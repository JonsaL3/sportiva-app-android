package es.dao.sportiva.webservice.empleado_inscribe_sesion

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.webservice.GenericRepo
import es.dao.sportiva.webservice.RetrofitSingleton

object EmpleadoInscribeSesionRepo {

    suspend fun findInscripcionesByIdSesion(
        idSesion: Int,
        context: Context? = null
    ): EmpleadoInscribeSesionWrapper? {

        val request = RetrofitSingleton.getRetrofitInstance()?.create(EmpleadoInscribeSesionService::class.java)?.findInscripcionesByIdSesion(idSesion)
        val response = GenericRepo.genericRequest(request, context)
        var empleadoInscribeSesion: EmpleadoInscribeSesionWrapper? = null

        response?.let { mResponse ->

            if (mResponse is EmpleadoInscribeSesionWrapper) {
                empleadoInscribeSesion = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        DxImplementation.mostrarDxWarning(
                            context = it,
                            mensaje = context.getString(R.string.no_hay_inscripciones)
                        )
                    }
                    else -> Log.e(":::FIND INSCRIPCIONES BY ID SESION", "Se ha producido un error desconocido")
                }

            }

        }

        return empleadoInscribeSesion
    }

}