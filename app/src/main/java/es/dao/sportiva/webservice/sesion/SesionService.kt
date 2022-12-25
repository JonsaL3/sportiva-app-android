package es.dao.sportiva.webservice.sesion

import android.database.Observable
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.sesion.SesionWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SesionService {

    @GET("sesion/findSesionesDisponibles/{idEmpresa}")
    fun findSesionesDisponibles(@Path("idEmpresa") idEmpresa: Int): Call<SesionWrapper>

    @GET("sesion/findSesionesDisponiblesByEntrenador/{idEntrenador}")
    fun findSesionesDisponiblesByEntrenador(@Path("idEntrenador") idEntrenador: Int): Call<SesionWrapper>

}