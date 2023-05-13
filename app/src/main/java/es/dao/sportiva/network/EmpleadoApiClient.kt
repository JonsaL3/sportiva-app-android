package es.dao.sportiva.network

import es.dao.sportiva.models.empleado.Empleado
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmpleadoApiClient {

    @POST("empleado/registrarEmpleado")
    fun registrarEmpleado(
        @Body empleado: Empleado
    ): Call<Boolean>

}