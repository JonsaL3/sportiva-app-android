package es.dao.sportiva.repository

import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.RegistroRequest
import es.dao.sportiva.models.usuario.UpdateProfilePictureRequest
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.network.UsuarioApiClient
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsuarioRepo @Inject constructor(
    private val usuarioApiClient: UsuarioApiClient,
) : GenericRepo() {

    suspend fun iniciarSesion(correo: String, contrasena: String): Usuario? {
        val iniciarSesionRequest = IniciarSesionRequest(
            correo = correo,
            contrasena = contrasena
        )
        val request = usuarioApiClient.iniciarSesion(iniciarSesionRequest)
        return genericRequest(request)?.getInstance()
    }

    suspend fun registerUsuario(request: RegistroRequest): Boolean? {
        val request = usuarioApiClient.registrarUsuario(request)
        return genericRequest(request)
    }

    suspend fun updateProfilePicture(request: UpdateProfilePictureRequest): Boolean? =
        genericRequest(usuarioApiClient.updateProfilePicture(request))


}