package es.dao.sportiva.webservice.empleado_inscribe_sesion

import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EmpleadoInscribeSesionService {

    @GET("empleadoInscribeSesion/findInscripcionesByIdSesion/{idSesion}")
    fun findInscripcionesByIdSesion(@Path("idSesion") idSesion: Int): Call<EmpleadoInscribeSesionWrapper>

}