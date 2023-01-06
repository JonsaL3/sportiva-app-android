package es.dao.sportiva.network.usuario

import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.IniciarSesionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioApiClient{

    @POST("usuario/iniciarSesion")
    fun iniciarSesion(@Body iniciarSesionRequest: IniciarSesionRequest): Call<IniciarSesionResponse>

}