package es.dao.sportiva.repository

import android.content.Context
import android.util.Log
import es.dao.sportiva.utils.DxImplementation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GenericRepo {

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend inline fun <T> genericRequest(
        request: Call<T>?,
        context: Context? = null
    ): Any? = suspendCancellableCoroutine { cc ->

        Log.d(":::GENERIC REQUEST", "Se ha lanzado la petición")
        request?.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                when (response?.code()) {
                    200 -> {
                        Log.d("::: GENERIC REPO RESPUESTA -> ", (response as T).toString())
                        cc.resume(response.body()) { } // TODO
                    }
                    else -> cc.resume(response?.code()) { } // TODO
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                if (t != null) {
                    Log.e("::: GENERIC REPO ERROR -> ", t.message ?: "Error desconocido")
                }
                context?.let { DxImplementation.mostrarDxErrorConexion(it) }
                return cc.resume(null) { } // TODO
            }

        }) ?: run {
            Log.e("::: GENERIC REPO ERROR -> ", "No se ha podido realizar la petición")
            context?.let { DxImplementation.mostrarDxErrorConexion(it) }
            return@run cc.resume(null) { } // TODO
        }

    }

}