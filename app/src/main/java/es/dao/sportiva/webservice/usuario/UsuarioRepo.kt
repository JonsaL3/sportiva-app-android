package es.dao.sportiva.webservice.usuario

import android.content.Context
import android.util.Log
import es.dao.sportiva.R
import es.dao.sportiva.models.Usuario
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.webservice.GenericRepo
import es.dao.sportiva.webservice.RetrofitSingleton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object UsuarioRepo {

    suspend fun iniciarSesion(
        iniciarSesionRequest: IniciarSesionRequest,
        context: Context? = null
    ): Usuario? {

        val request = RetrofitSingleton.getRetrofitInstance()?.create(UsuarioService::class.java)?.iniciarSesion(iniciarSesionRequest)
        val response = GenericRepo.genericRequest(request, context)
        var usuario: Usuario? = null

        response?.let { mResponse ->

            if (mResponse is IniciarSesionResponse) {
                usuario = mResponse.getInstance()
            } else {

                when (mResponse) {
                    404 -> context?.let {
                        DxImplementation.mostrarDxWarning(
                            context = it,
                            mensaje = it.getString(R.string.usuario_inexistente))
                    }
                    400 -> context?.let {
                        DxImplementation.mostrarDxWarning(
                            context = it,
                            mensaje = it.getString(R.string.creedenciales_incorrectas))
                    }
                    else -> Log.e(":::INICIAR SESION USUARIO REPO", "Se ha producido un error desconocido")
                }

            }

        }

        return usuario

    }

}