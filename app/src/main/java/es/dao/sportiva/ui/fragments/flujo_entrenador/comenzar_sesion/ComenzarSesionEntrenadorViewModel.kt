package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion

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
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComenzarSesionEntrenadorViewModel @Inject constructor(
    private val uiState: UiState,
    private val sesionRepo: SesionRepo,
    private val empleadoInscribeSesionRepo: EmpleadoInscribeSesionRepo
) : ViewModel() {

    /**
     * Todas las sesiones que ha creado el entrenador
     */
    private val _sesionesCreadasPorElEntrenador: MutableLiveData<List<Sesion>> = MutableLiveData()
    val sesionesCreadasPorElEntrenador: LiveData<List<Sesion>> = _sesionesCreadasPorElEntrenador

    /**
     * De todas las sesiones, se seleccionará una, la cual será almacenada aqui
     */
    private val _sesionLaCualSeDeseaComenzar: MutableLiveData<Sesion?> = MutableLiveData(null)
    val sesionLaCualSeDeseaComenzar: LiveData<Sesion?> = _sesionLaCualSeDeseaComenzar

    /**
     * Todas las inscripciones a esa sesión
     */
    private val _inscripcionesSesion: MutableLiveData<EmpleadoInscribeSesionWrapper?> = MutableLiveData(null)
    val inscripcionesSesion: LiveData<EmpleadoInscribeSesionWrapper?> = _inscripcionesSesion

    /**
     * Numero de personas que aún no han confirmado su asistencia a esa sesión
     */
    private var _pendientesPorConfirmar: MutableLiveData<Int> = MutableLiveData(0)
    val pendientesPorConfirmar: LiveData<Int> = _pendientesPorConfirmar

    /**
     * numero de personas que ya han confirmado su asistencia a esa sesión
     */
    private var _confirmados: MutableLiveData<Int> = MutableLiveData(0)
    val confirmados: LiveData<Int> = _confirmados

    /**
     * A lo largo de la vida del viewmodel, observaré siempre el numero de inscripciones
     */
    init {
        _inscripcionesSesion.observeForever { wrapper ->
            wrapper?.let { inscripciones ->
                _pendientesPorConfirmar.value = inscripciones.count { !it.isConfirmado }
                _confirmados.value = inscripciones.count { it.isConfirmado }
            }
        }
    }

    /**
     * Lo primero que necesito es descargarme todas las sesiones que creó ese entrenador
     */
    fun cargarDatosGenerales(idEntrenador: Int) = viewModelScope.launch {
        uiState.setLoading()
        sesionRepo.findSesionesDisponiblesByEntrenador(idEntrenador)?.let {
            uiState.setSuccess()
            _sesionesCreadasPorElEntrenador.postValue(it)
        }
    }

    /**
     * Desde la ui me vendrá la sesión que se desea comenzar
     */
    fun setSesion(sesion: Sesion) {
        _sesionLaCualSeDeseaComenzar.postValue(sesion)
        cargarInscripcionesSesion(sesion.id)
    }

    /**
     * Cuando se haya seleccionado la sesión, se descargará automáticamente las inscripciones a esa sesión
     */
    private fun cargarInscripcionesSesion(idSesion: Int) = viewModelScope.launch {
        uiState.setLoading()
        empleadoInscribeSesionRepo.findInscripcionesByIdSesion(idSesion)?.let {
            uiState.setSuccess()
            _inscripcionesSesion.postValue(it)
        }
    }

}