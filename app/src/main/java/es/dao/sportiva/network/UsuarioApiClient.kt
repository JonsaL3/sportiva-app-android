package es.dao.sportiva.network

import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.IniciarSesionResponse
import es.dao.sportiva.models.usuario.RegistroRequest
import es.dao.sportiva.models.usuario.UpdateProfilePictureRequest
import es.dao.sportiva.models.usuario.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UsuarioApiClient{

    @POST("usuario/iniciarSesion")
    fun iniciarSesion(@Body iniciarSesionRequest: IniciarSesionRequest): Call<IniciarSesionResponse>

    @POST("usuario/registrarUsuario")
    fun registrarUsuario(@Body usuario: RegistroRequest): Call<Boolean>

    @POST("usuario/updateProfilePicture")
    fun updateProfilePicture(@Body usuario: UpdateProfilePictureRequest): Call<Boolean>

}