package es.dao.sportiva.repository

import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.network.usuario.UsuarioService
import es.dao.sportiva.utils.UiState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepo @Inject constructor(
    private val usuarioService: UsuarioService,
    private var uiState: UiState
    ) {

    // En este proyecto no hay base de datos local, por lo que el repo en MVVM queda algo escueto.
    suspend fun iniciarSesion(iniciarSesionRequest: IniciarSesionRequest): Usuario? {
        // TODO HAGO LA PRUEBA DE PONER EL UISTATE EN ERROR, al ser un singleton
        // la main activity va a observar este cambio y MOSTRAR UN LOG DE ERROR
        uiState.setError()
        return usuarioService.iniciarSesion(iniciarSesionRequest)
    }

}