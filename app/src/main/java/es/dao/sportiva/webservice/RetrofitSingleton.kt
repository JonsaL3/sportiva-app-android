package es.dao.sportiva.webservice

import com.google.gson.GsonBuilder
import es.dao.sportiva.models.Usuario
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.webservice.serialization_utils.LocalDateTimeTypeAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime

object RetrofitSingleton {

    private var retrofit: Retrofit? = null

    fun getRetrofitInstance(): Retrofit? {

        val gsonBuilder = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())

            .create()

        retrofit?.let {
            return it
        } ?: run {
            retrofit = Retrofit.Builder()
                .baseUrl(Constantes.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
                .build()
            return retrofit
        }
    }

}