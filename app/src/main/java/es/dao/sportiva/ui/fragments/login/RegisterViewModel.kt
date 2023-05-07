package es.dao.sportiva.ui.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.repository.EmpleadoRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val empleadoRepo: EmpleadoRepo
): ViewModel() {

    var empleado: Empleado = Empleado()

    fun registerUser(
        onError: () -> Unit,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) = viewModelScope.launch{
        val response = empleadoRepo.registrarEmpleado(empleado)

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