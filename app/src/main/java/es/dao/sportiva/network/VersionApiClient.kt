package es.dao.sportiva.network

import es.dao.sportiva.models.version.Version
import retrofit2.Call
import retrofit2.http.GET

interface VersionApiClient{

    @GET("version/getLatestVersion")
    fun getLatestVersion(): Call<Version>

}