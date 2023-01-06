package es.dao.sportiva.network.entrenador

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.repository.GenericRepo
import es.dao.sportiva.utils.DxImplementation
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntrenadorService @Inject constructor(private val api: EntrenadorApiClient) {

    suspend fun findEntrenadoresByIdEmpresa(
        idEmpresa: Int,
        context: Context? = null
    ): EntrenadorWrapper? {

        val request = api.findEntrenadoresByIdEmpresa(idEmpresa)
        val response = GenericRepo.genericRequest(request, context)
        var entrenadoresEmpresa: EntrenadorWrapper? = null

        response?.let { mResponse ->

            if (mResponse is EntrenadorWrapper) {
                entrenadoresEmpresa = mResponse
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                    }
                    else -> {} // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                }

            }

        }

        return entrenadoresEmpresa
    }

}