package es.dao.sportiva.network

import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EmpleadoInscribeSesionApiClient {
    @GET("empleadoInscribeSesion/findInscripcionesByIdSesion/{idSesion}")
    fun findInscripcionesByIdSesion(@Path("idSesion") idSesion: Int): Call<EmpleadoInscribeSesionWrapper>
}