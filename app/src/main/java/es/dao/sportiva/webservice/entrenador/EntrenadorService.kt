package es.dao.sportiva.webservice.entrenador

import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EntrenadorService {

    @GET("entrenador/findByEmpresaAsignada/{idEmpresa}")
    fun findEntrenadoresByIdEmpresa(@Path("idEmpresa") idEmpresa: Int): Call<EntrenadorWrapper>

}