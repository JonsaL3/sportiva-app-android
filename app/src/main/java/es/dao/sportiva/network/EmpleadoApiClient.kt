package es.dao.sportiva.network

import es.dao.sportiva.models.Empleado
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.IniciarSesionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmpleadoApiClient {

    @POST("empleado/registrarEmpleado")
    fun registrarEmpleado(
        @Body empleado: Empleado
    ): Call<Boolean>

}