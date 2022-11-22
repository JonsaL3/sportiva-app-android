package es.dao.sportiva.webservice

import android.content.Context
import es.dao.sportiva.R
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

        request?.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                when (response?.code()) {
                    200 -> cc.resume(response.body()) { } // TODO
                    else -> cc.resume(response?.code()) { } // TODO
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                context?.let { DxImplementation.mostrarDxErrorConexion(it) }
                return cc.resume(null) { } // TODO
            }

        }) ?: run {
            context?.let { DxImplementation.mostrarDxErrorConexion(it) }
            return@run cc.resume(null) { } // TODO
        }

    }

}