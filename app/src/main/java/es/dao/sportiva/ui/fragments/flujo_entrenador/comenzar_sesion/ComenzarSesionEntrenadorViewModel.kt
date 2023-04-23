package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion

import android.database.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.enum.PasoFormularioComenzarSesion
import es.dao.sportiva.models.empleado.EmpleadoWrapper
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import es.dao.sportiva.models.empleado_participa_sesion.ComenzarSesionRequest
import es.dao.sportiva.models.empleado_participa_sesion.EmpleadoParticipaSesion
import es.dao.sportiva.models.empleado_participa_sesion.EmpleadoParticipaSesionWrapper
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.repository.EmpleadoInscribeSesionRepo
import es.dao.sportiva.repository.EmpleadoParticipaSesionRepo
import es.dao.sportiva.repository.SesionRepo
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorState
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ComenzarSesionEntrenadorViewModel @Inject constructor(
    private val uiState: UiState,
    private val sesionRepo: SesionRepo,
    private val empleadoInscribeSesionRepo: EmpleadoInscribeSesionRepo,
    private val empleadoParticipaSesion: EmpleadoParticipaSesionRepo
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
    private var _inscripciones: MutableLiveData<EmpleadoInscribeSesionWrapper> = MutableLiveData(EmpleadoInscribeSesionWrapper())
    val inscripciones: LiveData<EmpleadoInscribeSesionWrapper>
        get() = _inscripciones

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
        resetearPaso1()
        resetearPaso2()
    }

    private fun resetearPaso1() {
        _listaDeSesionesDisponibles.value = SesionWrapper()
        _sesionSeleccionada.value = null
    }

    fun resetearPaso2() {
        _inscripciones.value = EmpleadoInscribeSesionWrapper()
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
    fun obtenerInscripcionesSesion() = viewModelScope.launch {

        uiState.setLoading()

        _sesionSeleccionada.value?.let {

            empleadoInscribeSesionRepo.findInscripcionesByIdSesion(it.id).apply {
                _inscripciones.value = this
                uiState.setSuccess()
            }

        } ?: run {
            uiState.setError("No se ha seleccionado ninguna sesión.")
        }

    }

    // Dado un qr leido desde barcode compruebo que sus datos coincidan con el de
    // alguna inscripción de la sesión, y si es asi marco esa inscripción como asistida.
    // (Hasta que no se envien los datos al servidor realmente no se marcará como asistida, pero
    // se le pintará al usuario como asistida)
    fun marcarInscripcionDesdeQrIfCoincide(qr: String) {

        _inscripciones.value?.let { mInscripciones ->

            if (mInscripciones.isEmpty()) {
                uiState.setError("No puedes inscribir a nadie en esta sesión porque aún no se ha inscrito nadie a ella :C")
            } else {

                val empleadoCoincidenteConQr = mInscripciones.filter { inscripcion ->
                    inscripcion.empleadoInscrito.isScannedQrEqualsToThisUserToday(qr) &&
                    inscripcion.sesionALaQueSeInscribe.id == _sesionSeleccionada.value?.id // La select siempre me devuelve las sesiones que son iguales... pero porsi que se yo
                }.firstOrNull()

                empleadoCoincidenteConQr?.let {

                    if (it.isConfirmado) {
                        uiState.setError("El empleado que has escaneado ya ha confirmado su asistencia a esta sesión.")
                    } else {
                        empleadoCoincidenteConQr.isConfirmado = true
                        _inscripciones.value = mInscripciones // Notifico los cambios para que se entere el observer.
                    }

                } ?: run {
                    uiState.setError("El empleado que has escaneado no coincide con ninguno de los inscritos en esta sesión o escaneaste un código QR erroneo.")
                }

            }

        } ?: run {
            uiState.setError("No puedes inscribir a nadie en esta sesión porque aún no se ha inscrito nadie a ella :C")
        }

    }

    /**
     * Finalmente, damos comienzo a la sesión
     */
    fun comenzarSesion() = viewModelScope.launch {

        uiState.setLoading()

        _inscripciones.value?.let { mInscripciones ->

            // Comprobaciones previas
            if (_sesionSeleccionada.value == null) {
                uiState.setError("Error con la sesión seleccionada.")
                return@launch
            }

            if (mInscripciones.all { !it.isConfirmado }) {
                uiState.setError("Aún no has confirmado la asistencia de nadie a esta sesión, por lo que no se puede dar comienzo a la misma.")
                return@launch
            }

            if (mInscripciones.any { !it.isConfirmado }) {
                // TODO MENSAJE DE FALTAN ALGUNOS POR CONFIRMAR
            }

            // La marco como llevada a cabo, aunque esto se seteará en el servidor también
            _sesionSeleccionada.value!!.isLlevadaACabo = true

            // Preparo la insercción en el servidor de los empleados que participan relacionandolos con esta sesión el dia de hoy
            val request = ComenzarSesionRequest(
                sesion = _sesionSeleccionada.value!!,
                participaciones = EmpleadoWrapper(mInscripciones.map { it.empleadoInscrito }.toCollection(
                    arrayListOf()
                ))
            )

            empleadoParticipaSesion.comenzarSesion(request)?.apply {
                uiState.setSuccess()
                setState(
                    state = ComenzarSesionEntrenadorViewModelState.SesionCreadaCorrectamente(
                        sesion = this
                    )
                )
            } ?: run { }// No comprendo porque si falla se me metía al run de abajo.

        } ?: run {
            uiState.setError("No puedes comenzar una sesión si nadie se ha inscrito a ella aún.")
        }

    }

}

sealed class ComenzarSesionEntrenadorViewModelState {
    // Estados genéricos.
    object Neutral : ComenzarSesionEntrenadorViewModelState()
    data class IrAPasoEspecifico(
        val paso: PasoFormularioComenzarSesion
    ) : ComenzarSesionEntrenadorViewModelState()

    data class SesionCreadaCorrectamente(
        val sesion: EmpleadoParticipaSesionWrapper
    ) : ComenzarSesionEntrenadorViewModelState()

}