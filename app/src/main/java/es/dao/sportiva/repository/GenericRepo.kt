package es.dao.sportiva.repository

import android.util.Log
import es.dao.sportiva.utils.Constantes.GENERIC_TAG
import es.dao.sportiva.utils.Constantes.SERVER_HEADERS_ERROR_MESSAGE
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

open class GenericRepo {

    @Inject lateinit var uiState: UiState
    // Simplemente por comodidad, ya que tengo yo el control de como está creada la api, trato los errores
    // Directamente aqui, soy consciente que este tipo de cosas realmente deberían de estar en el viewmodel

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend inline fun <T> genericRequest(
        request: Call<T>?
    ): T? = suspendCancellableCoroutine { cc ->

        Log.d(GENERIC_TAG, "-> Se ha lanzado la petición ${request?.request()?.url()}\n" +
                 "-> Método ${request?.request()?.method()}\n" +
                 "-> Json ${request?.request()?.body()}"
        )
        request?.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                when (response?.code()) {
                    200 -> {
                        Log.d(GENERIC_TAG, "-> Respuesta correcta obtenida.\n" +
                                "-> Código ${response.code()}\n" +
                                "-> Json ${response.body().toString()}"
                        )
                        uiState.setSuccess()
                        cc.resume(response.body()) { } // TODO
                    }
                    else -> {
                        Log.d(GENERIC_TAG, "-> Error controlado por el servidor.\n" +
                                "-> Código ${response?.code()}\n" +
                                "-> Mensaje ${response?.message()}\n" +
                                "-> errorMessage ${response?.headers()?.get(SERVER_HEADERS_ERROR_MESSAGE)}\n" +
                                "-> Json ${response?.body()}"
                        )
                        uiState.setError(response?.headers()?.get(SERVER_HEADERS_ERROR_MESSAGE) ?: "Error desconocido")
                        cc.resume(null) { } // TODO
                    }
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                if (t != null) {
                    Log.d(GENERIC_TAG, "-> Error no controlado por el servidor.\n" +
                            "-> Exception message ${t.message}"
                    )
                    uiState.setError(t.message ?: "Error desconocido")
                }
                return cc.resume(null) { } // TODO
            }

        }) ?: run {
            Log.d(GENERIC_TAG, "-> Error en la petición. El valor de la petición es null.")
            uiState.setError("Error en la petición. El valor de la petición es null.")
            return@run cc.resume(null) { }
        }

    }

}