package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.repository.EntrenadorRepo
import es.dao.sportiva.repository.SesionRepo
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearSesionEntrenadorViewModel @Inject constructor(
    private var uiState: UiState,
    private val sesionRepo: SesionRepo,
    private val entrenadorRepo: EntrenadorRepo
) : ViewModel() {

    private var _state: MutableStateFlow<CrearSesionEntrenadorState> =
        MutableStateFlow(CrearSesionEntrenadorState.Neutral)
    val state: StateFlow<CrearSesionEntrenadorState>
        get() = _state

    private var _entrenadoresAnadidos: MutableLiveData<EntrenadorWrapper>
        = MutableLiveData(EntrenadorWrapper())
    val entrenadoresAnadidos: LiveData<EntrenadorWrapper>
        get() = _entrenadoresAnadidos

    fun setState(state: CrearSesionEntrenadorState) {
        _state.update { state }
    }

    fun addEntrenadores(entrenador: EntrenadorWrapper) {
        _entrenadoresAnadidos.value?.addAll(entrenador)
    }

    /**
     * Obtengo todos los entrenadores que pertenecen a la misma empresa que el entrenador
     * que trata de crear una sesión
     */
    fun findEntrenadoresByIdEmpresa(
        idEmpresa: Int
    ) = viewModelScope.launch {

        uiState.setLoading()

        entrenadorRepo.findEntrenadoresByIdEmpresa(idEmpresa)?.apply {
            uiState.setSuccess()
            setState(
                state = CrearSesionEntrenadorState.SeleccionandoEntrenador(
                    entrenadores = this
                )
            )
            setState(CrearSesionEntrenadorState.Neutral)
        }

    }

}

sealed class CrearSesionEntrenadorState {
    object Neutral : CrearSesionEntrenadorState()
    data class SeleccionandoEntrenador(val entrenadores: EntrenadorWrapper) : CrearSesionEntrenadorState()
}