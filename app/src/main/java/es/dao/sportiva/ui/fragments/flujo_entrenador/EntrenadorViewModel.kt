package es.dao.sportiva.ui.fragments.flujo_entrenador

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesionWrapper
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.repository.EmpleadoInscribeSesionRepo
import es.dao.sportiva.repository.SesionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenadorViewModel @Inject constructor(
    private val empleadoInscribeSesionRepo: EmpleadoInscribeSesionRepo,
    private val sesionRepo: SesionRepo
) : ViewModel() {

    // Datos generales del entrenador
    private var _sesionesCreadasPorElEntrenador: MutableLiveData<List<Sesion>?> = MutableLiveData(null)
    val sesionesCreadasPorElEntrenador: LiveData<List<Sesion>?> = _sesionesCreadasPorElEntrenador

    // Datoos necesarios para dar comienzo a una sesión
    private var _sesion: MutableLiveData<Sesion?> = MutableLiveData(null)
    val sesion: LiveData<Sesion?> = _sesion

    private val _inscripcionesSesion: MutableLiveData<EmpleadoInscribeSesionWrapper?> = MutableLiveData(null)
    val inscripcionesSesion: LiveData<EmpleadoInscribeSesionWrapper?> = _inscripcionesSesion

    // Información que necesito en tiempo real al comenzar una sesión
    private var _pendientesPorConfirmar: MutableLiveData<Int> = MutableLiveData(0)
    val pendientesPorConfirmar: LiveData<Int> = _pendientesPorConfirmar

    private var _confirmados: MutableLiveData<Int> = MutableLiveData(0)
    val confirmados: LiveData<Int> = _confirmados

    // Datos que necesito para crear una sesión
    private var _entrenadoresParticipantes: MutableLiveData<List<Entrenador>> = MutableLiveData(null)
    val entrenadoresParticipantes: LiveData<List<Entrenador>> = _entrenadoresParticipantes

    // Carga general de los datos

    init {
        _inscripcionesSesion.observeForever { inscripciones ->
            inscripciones?.let { wrapper ->
                _pendientesPorConfirmar.value = wrapper.count { !it.isConfirmado }
                _confirmados.value = wrapper.count { it.isConfirmado }
            }
        }
    }

    fun obtenerDatos(
        idEntrenador: Int,
        onLoadedAction: () -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {

        val job = CoroutineScope(Dispatchers.IO).launch {
            _sesionesCreadasPorElEntrenador.postValue(findSesionesDisponiblesByEntrenador(idEntrenador))
        }
        job.join()

        onLoadedAction.invoke()

    }

    // Manejo de la creacion de las sesiones
    fun addEntrenadorParticipante(entrenador: Entrenador) {
        val entrenadores = _entrenadoresParticipantes.value ?: emptyList()
        _entrenadoresParticipantes.value = entrenadores + entrenador
    }

    // Manejo del comienzo de las sesiones
    fun clearComienzoSesion() {
        _sesion.postValue(null)
        _inscripcionesSesion.postValue(null)
    }

    fun confirmarAsistencia() {

    }

    fun comenzarSesion() {

    }

    // Manejo de las sesiones que ha creado el deportista
    private suspend fun findSesionesDisponiblesByEntrenador(idEntrenador: Int): SesionWrapper? =
        sesionRepo.findSesionesDisponiblesByEntrenador(idEntrenador)

    fun setSesion(sesion: Sesion) {
        _sesion.value = sesion
    }

    // Manejo de las inscripciones de una sesión en concreto
    fun obtenerInscripcionesSesion() = viewModelScope.launch(Dispatchers.IO) {
        _sesion.value?.let { sesion ->
            _inscripcionesSesion.postValue(findInscripcionesByIdSesion(sesion.id))
        }
    }

    private suspend fun findInscripcionesByIdSesion(idSesion: Int): EmpleadoInscribeSesionWrapper? =
        empleadoInscribeSesionRepo.findInscripcionesByIdSesion(idSesion)
}