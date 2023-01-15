package es.dao.sportiva.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.utils.runOnUiThread
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.repository.UsuarioRepo
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usuarioRepo: UsuarioRepo,
    private val uiState: UiState
) : ViewModel() {

    private var _usuario: MutableLiveData<Usuario?> = MutableLiveData(null)
    val usuario: LiveData<Usuario?> = _usuario

    // Manejo del usuario que hizo login ############################################
    fun doLogin(
        correo: String,
        contrasena: String,
        context: Context,
        onSuccess: (usuario: Usuario) -> Unit,
    ) = viewModelScope.launch(Dispatchers.IO) {

        uiState.setLoading()

        CoroutineScope(Dispatchers.IO).launch {
            _usuario.postValue(usuarioRepo.iniciarSesion(correo, contrasena))
        }.invokeOnCompletion {
            _usuario.value?.let { usuario ->
                runOnUiThread {
                    uiState.setSuccess()
                    onSuccess.invoke(usuario)
                }
            }
        }

    }

    fun doLogout() {
        _usuario.postValue(null)
    }

}