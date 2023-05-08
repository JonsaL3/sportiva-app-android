package es.dao.sportiva.network

import es.dao.sportiva.models.empleado_participa_sesion.ComenzarSesionRequest
import es.dao.sportiva.models.empleado_participa_sesion.EmpleadoParticipaSesionWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmpleadoParticipaSesionApiClient {

    @POST("empleadoParticipaSesion/comenzarSesion")
    fun comenzarSesion(@Body eps: ComenzarSesionRequest): Call<EmpleadoParticipaSesionWrapper>

}