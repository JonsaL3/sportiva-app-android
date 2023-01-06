package es.dao.sportiva.network.sesion

import es.dao.sportiva.models.sesion.SesionWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SesionApiClient {

    @GET("sesion/findSesionesDisponibles/{idEmpresa}")
    fun findSesionesDisponibles(@Path("idEmpresa") idEmpresa: Int): Call<SesionWrapper>

    @GET("sesion/findSesionesDisponiblesByEntrenador/{idEntrenador}")
    fun findSesionesDisponiblesByEntrenador(@Path("idEntrenador") idEntrenador: Int): Call<SesionWrapper>

}