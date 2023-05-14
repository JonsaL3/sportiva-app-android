package es.dao.sportiva.network

import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesion
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EmpleadoInscribeSesionApiClient {
    @GET("empleadoInscribeSesion/findInscripcionesByIdSesion/{idSesion}")
    fun findInscripcionesByIdSesion(@Path("idSesion") idSesion: Int): Call<EmpleadoInscribeSesionWrapper>

    @POST("empleadoInscribeSesion/inscribirEmpleadoASesion")
    fun inscribirEmpleadoASesion(@Body inscripcion: EmpleadoInscribeSesion): Call<EmpleadoInscribeSesion>

    @DELETE("empleadoInscribeSesion/desinscribirEmpleadoASesion/{idSesion}/{idEmpleado}")
    fun desinscribirEmpleadoASesion(@Path("idSesion") idSesion: Int, @Path("idEmpleado") idEmpleado: Int): Call<EmpleadoInscribeSesion>

}