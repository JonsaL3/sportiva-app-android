package es.dao.sportiva.ui.fragments.flujo_empleado

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesion
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.repository.EmpleadoInscribeSesionRepo
import es.dao.sportiva.repository.EntrenadorRepo
import es.dao.sportiva.repository.SesionRepo
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class EmpleadoViewModel @Inject constructor(
    private val sesionRepo: SesionRepo,
    private val entrenadorRepo: EntrenadorRepo,
    private val empleadoInscribeSesionRepo: EmpleadoInscribeSesionRepo,
    private val uiState: UiState
) : ViewModel() {

    /**
     * Sesiones a las que se puede inscribir el usuario.
     */
    private var _sesionesDisponibles: MutableLiveData<SesionWrapper?> = MutableLiveData(null)
    val sesionesDisponibles: LiveData<SesionWrapper?> = _sesionesDisponibles

    /**
     * Entrenadores asignados ese dia a la misma empresa que el usuario.
     */
    private var _entrenadores: MutableLiveData<EntrenadorWrapper?> = MutableLiveData(null)
    val entrenadores: LiveData<EntrenadorWrapper?> = _entrenadores

    /**
     * Estados del fragmento
     */
    private var _state: MutableStateFlow<EmpleadoViewModelState> =
        MutableStateFlow(EmpleadoViewModelState.Neutral)
    val state: StateFlow<EmpleadoViewModelState> = _state

    /**
     * Gestion de jobs
     */
    private var runningJobs: MutableSet<Job> = mutableSetOf()

    /**
     * Debo de poder cambiar de estado
     */
    fun setState(state: EmpleadoViewModelState) {
        viewModelScope.launch {
            _state.emit(state)
        }.invokeOnCompletion {
            viewModelScope.launch { _state.emit(EmpleadoViewModelState.Neutral) }
        }
    }

    /**
     * Debo poder vaciar el viewmodel puesto que lo instancio como activityViewModels
     */
    fun resetViewModel() {
        _sesionesDisponibles.postValue(null)
        _entrenadores.postValue(null)
        runningJobs.forEach { it.cancel() }
        runningJobs.clear()
    }

    /**
     * Me descargo la lista de sesiones disponibles y los entrenadores asignados a la empresa
     */
    fun obtenerDatos(idEmpresa: Int, idEmpleado: Int) {

        val trabajo = viewModelScope.launch(Dispatchers.IO) {

            uiState.setLoading()

            val job = CoroutineScope(Dispatchers.IO).launch {
                _sesionesDisponibles.postValue(
                    sesionRepo.findSesionesDisponibles(
                        idEmpresa,
                        idEmpleado
                    )
                )
                _entrenadores.postValue(entrenadorRepo.findEntrenadoresByIdEmpresa(idEmpresa))
            }

            job.join()
            uiState.setSuccess()

        }

        runningJobs.add(trabajo)
        trabajo.invokeOnCompletion { runningJobs.remove(trabajo) }

    }

    /**
     * Si me inscribo a una sesión reflejo ese cambio en el servidor
     */
    fun inscribirEmpleadoASesion(
        empleado: Empleado,
        sesion: Sesion,
    ) {

        val job = viewModelScope.launch {

            uiState.setLoading()

            val inscripcion = EmpleadoInscribeSesion(
                empleadoInscrito = empleado,
                sesionALaQueSeInscribe = sesion,
                fechaInscripcion = LocalDateTime.now(),
            )

            empleadoInscribeSesionRepo.inscribirEmpleadoASesion(inscripcion)?.apply {
                uiState.setSuccess()
                // Seteo a true la inscripcion en la sesión que acabo de inscribirme
                // en la lista de sesiones
                _sesionesDisponibles.value?.find { it.id == sesion.id }?.isCurrentEmpleadoInscrito =
                    true

                // Sumo 1 unidad en el numero de inscripciones si el aforo no es ilimitado
                if (this.sesionALaQueSeInscribe.aforoMaximo != Constantes.AFORO_ILIMITADO) {
                    sesionesDisponibles.value?.find { it.id == sesion.id }?.let {
                        val inscripcionesActuales = it.numeroDeInscripciones + 1
                        it.numeroDeInscripciones = inscripcionesActuales
                    }
                }

                // hago un post value para que el recycler se actualice
                _sesionesDisponibles.postValue(_sesionesDisponibles.value)

                // Notifico el estado al fragmento
                setState(EmpleadoViewModelState.ApuntadoCorrectamente(this.sesionALaQueSeInscribe))
            }

        }

        runningJobs.add(job)
        job.invokeOnCompletion { runningJobs.remove(job) }

    }

    /**
     * Lo mismo cuando me desinscribo.
     */
    fun desinscribirEmpleadoASesion(
        idEmpleado: Int,
        idSesion: Int
    ) {

        val job = viewModelScope.launch {

            uiState.setLoading()

            empleadoInscribeSesionRepo.desinscribirEmpleadoASesion(
                idSesion = idSesion,
                idEmpleado = idEmpleado
            )?.apply {
                uiState.setSuccess()
                // Seteo a true la inscripcion en la sesión que acabo de inscribirme
                // en la lista de sesiones
                _sesionesDisponibles.value?.find { it.id == idSesion }?.isCurrentEmpleadoInscrito =
                    false

                // Resto 1 unidad en el numero de inscripciones si el aforo no es ilimitado
                if (this.sesionALaQueSeInscribe.aforoMaximo != Constantes.AFORO_ILIMITADO) {
                    sesionesDisponibles.value?.find { it.id == idSesion }?.let {
                        val inscripcionesActuales = it.numeroDeInscripciones - 1
                        it.numeroDeInscripciones = inscripcionesActuales
                    }
                }

                // hago un post value para que el recycler se actualice
                _sesionesDisponibles.postValue(_sesionesDisponibles.value)

                // Notifico el estado al fragmento
                setState(EmpleadoViewModelState.DesapuntadoCorrectamente(this.sesionALaQueSeInscribe))
            }

        }

        runningJobs.add(job)
        job.invokeOnCompletion { runningJobs.remove(job) }


    }
}

/**
 * Este fragmento necesita realmente muy pocos estados, simplemente para mostrar algun mensaje de exito
 * y cosas por el esitlo, porque cuando llega una lista del servidor etc realmente ese cambio es notficado
 * por livedata. El resto de estados genericos ya se gestionan por la clase UISTATE.
 */
sealed class EmpleadoViewModelState {
    object Neutral : EmpleadoViewModelState()
    data class ApuntadoCorrectamente(val sesion: Sesion) : EmpleadoViewModelState()
    data class DesapuntadoCorrectamente(val sesion: Sesion) : EmpleadoViewModelState()
}