package es.dao.sportiva.network

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.utils.LocalDateTimeTypeAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Esto no tiene nada que ver con un Singleton, estoy indicando que todos los inject que declare aqui serán a nivel da aplicacion.
class NetworkModule {

    // Objetos genericos necesarios en forma de singleton para las conexiones #####################
    @Singleton // Ahora si, especifico que solo me cree una instancia a nivel de la aplicación
    @Provides
    fun provideRetrofit(): Retrofit {
        val gsonBuilder = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .create()
        return Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .build()
    }

    // Implementación de las interfaces de retrofit (Las que he llamado api client) ###############
    @Singleton
    @Provides                  // Inyecto el retrofit proveido con provideRetrofit
    fun provideUsuarioApiClient(retrofit: Retrofit) : UsuarioApiClient =
        retrofit.create(UsuarioApiClient::class.java)

    @Singleton
    @Provides
    fun provideSesionApiClient(retrofit: Retrofit): SesionApiClient =
        retrofit.create(SesionApiClient::class.java)

    @Singleton
    @Provides
    fun provideEntrenadorApiClient(retrofit: Retrofit): EntrenadorApiClient =
        retrofit.create(EntrenadorApiClient::class.java)

    @Singleton
    @Provides
    fun provideEmpleadoInscribeSesionApiClient(retrofit: Retrofit): EmpleadoInscribeSesionApiClient =
        retrofit.create(EmpleadoInscribeSesionApiClient::class.java)

}