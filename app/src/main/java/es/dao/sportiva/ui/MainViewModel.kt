package es.dao.sportiva.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.utils.runOnUiThread
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.repository.UsuarioRepo
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
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

        val EMPRESA_EJEMPLO_1 = Empresa(
            id = 1,
            nombre = "PEPOTE SL",
            isActivo = true
        )

        val EMPLEADO_EJEMPLO_1 = Empleado(
            id = 1,
            correo = "gonzalo@gonzalo.es",
            contrasena = "1234",
            nombre = "Gonzalo",
            apellido1 = "Racero",
            apellido2 = "Galán",
            fechaNacimiento = LocalDateTime.of(2000, 5, 8, 0, 0),
            fechaInserccion = LocalDateTime.now(),
            isActivo = true,
            imagen = null,
            cargo = "Programador full stack android",
            peso = 60.0f,
            altura = 1.70f,
            isDeporteFrecuente = false,
            isFumador = false,
            empresa = EMPRESA_EJEMPLO_1
        )

        CoroutineScope(Dispatchers.IO).launch {
            launch {
//                _usuario.postValue(usuarioRepo.iniciarSesion(correo, contrasena))
                _usuario.postValue(EMPLEADO_EJEMPLO_1)
            }.join()
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