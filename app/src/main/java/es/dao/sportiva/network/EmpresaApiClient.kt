package es.dao.sportiva.network

import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.IniciarSesionResponse
import es.dao.sportiva.models.usuario.RegistroRequest
import es.dao.sportiva.models.usuario.Usuario
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EmpresaApiClient{

    @GET("empresa/findAll")
    fun findAll(): Call<List<Empresa>>

}