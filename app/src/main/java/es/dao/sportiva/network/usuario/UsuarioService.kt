package es.dao.sportiva.network.usuario

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.IniciarSesionResponse
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.repository.GenericRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioService @Inject constructor(private val api: UsuarioApiClient) {

    suspend fun iniciarSesion(
        iniciarSesionRequest: IniciarSesionRequest,
        context: Context? = null
    ): Usuario? {

        val request = api.iniciarSesion(iniciarSesionRequest)
        val response = GenericRepo.genericRequest(request, context)
        var usuario: Usuario? = null

        response?.let { mResponse ->

            if (mResponse is IniciarSesionResponse) {
                usuario = mResponse.getInstance()
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                    }
                    400 -> context?.let {
                        // TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                    }
                    else -> {}// TODO USAR LA CLASE UI STATE INYECTADA EN FORMA DE SINGLETON (seguramente delegue al repo el tratamiento de estos errores)
                }

            }

        }

        return usuario

    }

}