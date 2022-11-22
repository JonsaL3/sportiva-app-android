package es.dao.sportiva.webservice.usuario

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UsuarioService {

    @POST("usuario/iniciarSesion")
    fun iniciarSesion(@Body iniciarSesionRequest: IniciarSesionRequest): Call<IniciarSesionResponse>

}