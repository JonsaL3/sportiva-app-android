package es.dao.sportiva.network

import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EntrenadorApiClient {

    @GET("entrenador/findByEmpresaAsignada/{idEmpresa}")
    fun findEntrenadoresByIdEmpresa(@Path("idEmpresa") idEmpresa: Int): Call<EntrenadorWrapper>

    @POST("empleado/registrarEntrenador")
    fun registrarEntrenador(
        @Body entrenador: Entrenador
    ): Call<Boolean>
}