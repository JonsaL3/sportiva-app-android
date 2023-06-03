package es.dao.sportiva.ui.fragments.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.usuario.RegistroRequest
import es.dao.sportiva.repository.EmpresaRepo
import es.dao.sportiva.repository.UsuarioRepo
import es.dao.sportiva.utils.LocalDateTimeTypeAdapter
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val usuarioRepo: UsuarioRepo,
    private val empresaRepo: EmpresaRepo,
): ViewModel() {

    var empleado: Empleado = Empleado()
    var entrenador: Entrenador = Entrenador()
    var empresa: Empresa = Empresa()
    var profliePictureUri: String? = null
    var birthDate: LocalDateTime? = null

    fun registerEmpleado(
        onError: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) = viewModelScope.launch{

        empleado.isActivo = true
        empresa.isActivo = true
        empleado.empresa = empresa
        empleado.imagen = null

        val response = usuarioRepo.registerUsuario(
            RegistroRequest(
                isEmpleado = true,
                json = GsonBuilder().serializeNulls()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                    .create().toJson(empleado)
            )
        )

        response?.let{
            if(it){
                onSuccess()
            }else{
                onError()
            }
        }?:run{
            onFailure()
        }

    }
    
    fun registerEntrenador(
        onError: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) = viewModelScope.launch{

        Log.d(";;;", GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
            .create().toJson(entrenador))

        val response = usuarioRepo.registerUsuario(
            RegistroRequest(
                isEmpleado = false,
                json = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
                    .create().toJson(entrenador)
            )
        )

        response?.let{
            if(it){
                onSuccess()
            }else{
                onError()
            }

        }?:run{
            onFailure()
        }
    }

    fun getEmpresas(onSuccess: (List<Empresa>?) -> Unit) = viewModelScope.launch {
        onSuccess(empresaRepo.findAll())
    }
}