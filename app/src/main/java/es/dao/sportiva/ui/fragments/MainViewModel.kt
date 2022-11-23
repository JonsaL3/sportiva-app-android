package es.dao.sportiva.ui.fragments

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dao.sportiva.models.Usuario
import es.dao.sportiva.utils.runOnUiThread
import es.dao.sportiva.webservice.usuario.IniciarSesionRequest
import es.dao.sportiva.webservice.usuario.UsuarioRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var _usuario: MutableLiveData<Usuario?> = MutableLiveData(null)
    val usuario: LiveData<Usuario?> = _usuario

    // Manejo del usuario que hizo login ############################################
    fun doLogin(
        correo: String,
        contrasena: String,
        context: Context,
        onSuccess: (usuario: Usuario) -> Unit,
    ) {
        val iniciarSesionRequest = IniciarSesionRequest(
            correo = correo,
            contrasena = contrasena
        )

        viewModelScope.launch {

            val job = CoroutineScope(Dispatchers.IO).launch {
                val usuario = UsuarioRepo.iniciarSesion(
                    iniciarSesionRequest =iniciarSesionRequest,
                    context = context
                )
                _usuario.postValue(usuario)
            }

            job.join() // no ejecuto el onSuccess hasta que termine el job

            _usuario.value?.let { usuario ->
                runOnUiThread {
                    onSuccess.invoke(usuario)
                }
            }

        }

    }

    fun doLogout() {
        _usuario.postValue(null)
    }

}