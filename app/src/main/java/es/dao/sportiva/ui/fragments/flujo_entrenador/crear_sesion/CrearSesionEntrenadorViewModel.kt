package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.enum.PasoFormularioCrearSesion
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.repository.EntrenadorRepo
import es.dao.sportiva.repository.SesionRepo
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.utils.UiState
import es.dao.sportiva.utils.toBase64
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CrearSesionEntrenadorViewModel @Inject constructor(
    private var uiState: UiState,
    private val sesionRepo: SesionRepo,
    private val entrenadorRepo: EntrenadorRepo
) : ViewModel() {

    /**
     * Estados
     */
    private var _state: MutableStateFlow<CrearSesionEntrenadorState> = MutableStateFlow(CrearSesionEntrenadorState.Neutral)
    val state = _state.asStateFlow()

    /**
     * Datos del paso 1 del formulario (Añadir entrenadores participantes)
     */
    private var _entrenadoresAnadidos: MutableLiveData<EntrenadorWrapper>
        = MutableLiveData(EntrenadorWrapper())
    val entrenadoresAnadidos: LiveData<EntrenadorWrapper>
        get() = _entrenadoresAnadidos

    /**
     * Datos del paso 2 del formulario (Información general)
     */
    private var _tituloSesion: MutableLiveData<String> = MutableLiveData("")
    val tituloSesion: LiveData<String>
        get() = _tituloSesion

    private var _subtituloSesion: MutableLiveData<String> = MutableLiveData("")
    val subtituloSesion: LiveData<String>
        get() = _subtituloSesion

    private var _resumenSesion: MutableLiveData<String> = MutableLiveData("")
    val resumenSesion: LiveData<String>
        get() = _resumenSesion

    /**
     * Datos del paso 3 del formulario
     */
    private var _fecha: MutableLiveData<LocalDate?> = MutableLiveData(null)
    val fecha: LiveData<LocalDate?>
        get() = _fecha

    private var _hora: MutableLiveData<LocalTime?> = MutableLiveData(null)
    val hora: LiveData<LocalTime?>
        get() = _hora

    /**
     * Datos del paso 4 del formulario
     */
    private var _aforo: MutableLiveData<Int> = MutableLiveData(Constantes.AFORO_ILIMITADO)
    val aforo: LiveData<Int>
        get() = _aforo

    /**
     * Datos del paso 5 del formulario
     */
    private var _uriFromDispositivo: MutableLiveData<String> = MutableLiveData("")
    val uriFromDispositivo: LiveData<String>
        get() = _uriFromDispositivo

    private var _uriRecienRealizada: MutableLiveData<String> = MutableLiveData("")
    val uriRecienRealizada: LiveData<String>
        get() = _uriRecienRealizada

    /**
     * Debo poder modificar el estado de mi ui desde donde sea
     */
    fun setState(state: CrearSesionEntrenadorState) = viewModelScope.launch {
        launch {
            _state.emit(state)
        }.invokeOnCompletion { launch { _state.emit(CrearSesionEntrenadorState.Neutral) } }
    }

    /**
     * reseteo todos los campos que ha ido rellenando el usuario
     */
    fun resetViewModel() {
        _state.value = CrearSesionEntrenadorState.Neutral
        _entrenadoresAnadidos.value = EntrenadorWrapper()
        _tituloSesion.value = ""
        _subtituloSesion.value = ""
        _resumenSesion.value = ""
        _fecha.value = null
        _hora.value = null
        _aforo.value = Constantes.AFORO_ILIMITADO
        _uriFromDispositivo.value = ""
        _uriRecienRealizada.value = ""
    }

    /**
     * Métodos del paso 1
     */
    fun addEntrenadores(entrenadores: EntrenadorWrapper) {
        _entrenadoresAnadidos.value!!.addAll(entrenadores)
        _entrenadoresAnadidos.value = _entrenadoresAnadidos.value
    }

    fun removeEntrenador(entrenador: Entrenador) {
        _entrenadoresAnadidos.value?.remove(entrenador)
        _entrenadoresAnadidos.value = _entrenadoresAnadidos.value
    }
    fun findEntrenadoresByIdEmpresa(
        idEmpresa: Int
    ) = viewModelScope.launch {

        uiState.setLoading()
        entrenadorRepo.findEntrenadoresByIdEmpresa(idEmpresa)?.apply {
            uiState.setSuccess()
            setState(CrearSesionEntrenadorState.SeleccionandoEntrenador(this))
        }

    }

    /**
     * Métodos del paso 2
     */
    fun setTituloSesion(titulo: String) {
        _tituloSesion.value = titulo
    }

    fun setSubtituloSesion(subtitulo: String) {
        _subtituloSesion.value = subtitulo
    }

    fun setResumenSesion(resumen: String) {
        _resumenSesion.value = resumen
    }

    fun camposDelPaso2Validos() = _tituloSesion.value!!.isNotEmpty() &&
            _subtituloSesion.value!!.isNotEmpty() &&
            _resumenSesion.value!!.isNotEmpty()

    /**
     * Métodos del paso 3
     */
    fun setFecha(fecha: LocalDate) {
        _fecha.value = fecha
    }

    fun setHora(hora: LocalTime) {
        _hora.value = hora
    }

    fun camposDelPaso3Validos(): Int {

        if (_fecha.value == null || _hora.value == null) {
            return -1
        }

        val localDateTime = LocalDateTime.of(_fecha.value!!, _hora.value!!)
        val now = LocalDateTime.now()

        return if (localDateTime.isBefore(now)) {
            -2
        } else {
            0
        }

    }

    /**
     * Métodos del paso 4
     */
    fun setAforo(aforo: Int) {
        _aforo.value = aforo
    }

    fun setAforoIlimitado() {
        _aforo.value = Constantes.AFORO_ILIMITADO
    }

    fun setAforoNoDefinido() {
        _aforo.value = Constantes.AFORO_NO_DEFINIDO
    }

    fun comprobarCamposDelPaso4() = when(_aforo.value) {
        Constantes.AFORO_ILIMITADO -> true
        Constantes.AFORO_NO_DEFINIDO -> false
        else -> _aforo.value!! > 0
    }

    /**
     * Métodos del paso 5
     */
    fun setUriFromDispositivo(uri: String) {
        _uriFromDispositivo.value = uri
        _uriRecienRealizada.value = ""
    }

    fun setUriRecienRealizada(uri: String) {
        _uriRecienRealizada.value = uri
        _uriFromDispositivo.value = ""
    }

    fun camposDelPaso5Validos() =
        _uriFromDispositivo.value!!.isNotEmpty() || _uriRecienRealizada.value!!.isNotEmpty()

    /**
     * Creación de la sesión y subida de la misma al servidor
     */
    fun crearSesion(context: Context, empresa: Empresa, creador: Entrenador) = viewModelScope.launch {

        uiState.setLoading()

        val sesion = Sesion(
            titulo = _tituloSesion.value!!,
            subtitulo = _subtituloSesion.value!!,
            descripcion = resumenSesion.value!!,
            fechaInserccion = LocalDateTime.now(),
            fechaSesion = LocalDateTime.of(_fecha.value!!, _hora.value!!),
            aforoMaximo = _aforo.value!!,
            imagen = Uri.parse(_uriRecienRealizada.value!!.ifEmpty { _uriFromDispositivo.value!! }).toBase64(context),
            empresa = empresa,
            creador = creador
        )

        sesionRepo.crearSesion(sesion)?.apply {
            uiState.setSuccess()
            setState(CrearSesionEntrenadorState.SesionCreadaCorrectamente(this))
        }

    }

}

/**
 * Estados a lo largo de la creación de una sesión
 */
sealed class CrearSesionEntrenadorState {
    object Neutral : CrearSesionEntrenadorState()

    // Navegación entre los diferentes pasos del formulario de crear una sesión
    data class SolicitarInformacion(val paso: PasoFormularioCrearSesion) : CrearSesionEntrenadorState()

    // Hago de la descarga de entrenadores un estado para simplificar las cosas.
    data class SeleccionandoEntrenador(val entrenadores: EntrenadorWrapper) : CrearSesionEntrenadorState()

    // Finalizo la creación de la sesión y lo envío al servidor si todho fué correctamente
    object CrearSesion : CrearSesionEntrenadorState()
    data class SesionCreadaCorrectamente(val sesion: Sesion) : CrearSesionEntrenadorState()

}