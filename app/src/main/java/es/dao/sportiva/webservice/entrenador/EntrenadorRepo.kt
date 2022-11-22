package es.dao.sportiva.webservice.entrenador

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.webservice.GenericRepo
import es.dao.sportiva.webservice.RetrofitSingleton

object EntrenadorRepo {

    suspend fun findEntrenadoresByIdEmpresa(
        idEmpresa: Int,
        context: Context? = null
    ): EntrenadorWrapper? {

        val request = RetrofitSingleton.getRetrofitInstance()?.create(EntrenadorService::class.java)?.findEntrenadoresByIdEmpresa(idEmpresa)
        val response = GenericRepo.genericRequest(request, context)
        var entrenadoresEmpresa: EntrenadorWrapper? = null

        response?.let { mResponse ->

            if (mResponse is EntrenadorWrapper) {
                entrenadoresEmpresa = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        DxImplementation.mostrarDxWarning(
                            context = it,
                            mensaje = context.getString(R.string.no_se_han_encontrado_sesiones)
                        )
                    }
                    else -> Log.e(":::FIND ENTRENADORES ENTRENADOR REPO", "Se ha producido un error desconocido")
                }

            }

        }

        return entrenadoresEmpresa
    }

}