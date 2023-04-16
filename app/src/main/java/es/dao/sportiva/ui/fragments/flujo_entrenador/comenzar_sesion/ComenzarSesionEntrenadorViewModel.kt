package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion

import android.database.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.enum.PasoFormularioComenzarSesion
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import es.dao.sportiva.models.empleado_participa_sesion.EmpleadoParticipaSesion
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.repository.EmpleadoInscribeSesionRepo
import es.dao.sportiva.repository.SesionRepo
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorState
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComenzarSesionEntrenadorViewModel @Inject constructor(
    private val uiState: UiState,
    private val sesionRepo: SesionRepo,
    private val empleadoInscribeSesionRepo: EmpleadoInscribeSesionRepo
) : ViewModel() {

    /**
     * Estados
     */
    private var _state: MutableStateFlow<ComenzarSesionEntrenadorViewModelState> = MutableStateFlow(ComenzarSesionEntrenadorViewModelState.Neutral)
    val state = _state.asStateFlow()

    /**
     * Elementos del paso 1 del formulario
     */
    private val _listaDeSesionesDisponibles: MutableLiveData<SesionWrapper> = MutableLiveData(SesionWrapper())
    val listaDeSesionesDisponibles: LiveData<SesionWrapper>
        get() = _listaDeSesionesDisponibles

    private var _sesionSeleccionada: MutableLiveData<Sesion?> = MutableLiveData(null)
    val sesionSeleccionada: LiveData<Sesion?>
        get() = _sesionSeleccionada

    /**
     * Elementos del paso 2 del formulario
     */
    private var _asistenciasConfirmadas: MutableLiveData<MutableList<EmpleadoParticipaSesion>> = MutableLiveData(mutableListOf())
    val asistenciasConfirmadas: LiveData<MutableList<EmpleadoParticipaSesion>>
        get() = _asistenciasConfirmadas

    /**
     * Debo poder modificar el estado de mi ui desde donde sea
     */
    fun setState(state: ComenzarSesionEntrenadorViewModelState) = viewModelScope.launch {
        launch {
            _state.emit(state)
        }.invokeOnCompletion { launch { _state.emit(ComenzarSesionEntrenadorViewModelState.Neutral) } }
    }

    /**
     * Reseteo el viewmodel
     */
    fun resetViewModel() {
        _sesionSeleccionada.value = null
        _asistenciasConfirmadas.value = mutableListOf()
    }

    /**
     * Métodos del paso 1 del formulario
     */
    fun obtenerSesionesDisponibles(idEntrenador: Int) = viewModelScope.launch {
        uiState.setLoading()
        sesionRepo.findSesionesDisponiblesByEntrenador(idEntrenador)?.apply {
            _listaDeSesionesDisponibles.value = this
            uiState.setSuccess()
        }
    }

    fun setSesionSeleccionada(sesion: Sesion) {
        _sesionSeleccionada.value = sesion
        setState(
            state = ComenzarSesionEntrenadorViewModelState.IrAPasoEspecifico(
                paso = PasoFormularioComenzarSesion.CONFIRMAR_ASISTENCIA
            )
        )
    }

    /**
     * Métodos del paso 2 del formulario
     */
    fun anadirAsistencia(empleado: EmpleadoParticipaSesion) {
        _asistenciasConfirmadas.value!!.add(empleado)
        _asistenciasConfirmadas.value = _asistenciasConfirmadas.value
    }

    /**
     * Finalmente, damos comienzo a la sesión
     */
    fun comenzarSesion() {
        // TODO
    }

}

sealed class ComenzarSesionEntrenadorViewModelState {
    // Estados genéricos.
    object Neutral : ComenzarSesionEntrenadorViewModelState()
    data class IrAPasoEspecifico(val paso: PasoFormularioComenzarSesion) : ComenzarSesionEntrenadorViewModelState()

}