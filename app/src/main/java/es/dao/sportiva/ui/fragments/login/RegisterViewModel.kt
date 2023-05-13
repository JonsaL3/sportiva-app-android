package es.dao.sportiva.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.repository.EmpleadoRepo
import es.dao.sportiva.repository.EntrenadorRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val empleadoRepo: EmpleadoRepo,
    private val entrenadorRepo: EntrenadorRepo
): ViewModel() {

    var empleado: Empleado = Empleado()
    var entrenador: Entrenador = Entrenador()

    fun registerEmpleado(
        onError: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) = viewModelScope.launch{
        val response = empleadoRepo.registerEmpleado(empleado)

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
        val response = entrenadorRepo.registerEntrenador(entrenador)

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

}