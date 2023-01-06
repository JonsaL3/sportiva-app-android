package es.dao.sportiva.ui.fragments.flujo_empleado

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.repository.EntrenadorRepo
import es.dao.sportiva.repository.SesionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmpleadoViewModel @Inject constructor(
    private val sesionRepo: SesionRepo,
    private val entrenadorRepo: EntrenadorRepo
) : ViewModel() {

    private var _sesionesDisponibles: MutableLiveData<SesionWrapper?> = MutableLiveData(null)
    val sesionesDisponibles: LiveData<SesionWrapper?> = _sesionesDisponibles

    private var _entrenadores: MutableLiveData<EntrenadorWrapper?> = MutableLiveData(null)
    val entrenadores: LiveData<EntrenadorWrapper?> = _entrenadores

    fun obtenerDatos(idEmpresa: Int) = viewModelScope.launch(Dispatchers.IO) {

        val job = CoroutineScope(Dispatchers.IO).launch {
            _sesionesDisponibles.postValue(sesionRepo.findSesionesDisponibles(idEmpresa))
            _entrenadores.postValue(entrenadorRepo.findEntrenadoresByIdEmpresa(idEmpresa))
        }

        job.join()

        // TODO AQUI TERMINAR ANIMACION DE CARGA

    }

    // Manejo de las sesiones disponibles ##########################################
    private suspend fun findSesionesDisponibles(idEmpresa: Int): SesionWrapper? =
        sesionRepo.findSesionesDisponibles(idEmpresa)

    // Manejo de los entrenadores asignados a la empresa ##################################################
    private suspend fun findEntrenadoresByIdEmpresa(idEmpresa: Int): EntrenadorWrapper? =
        entrenadorRepo.findEntrenadoresByIdEmpresa(idEmpresa)

}