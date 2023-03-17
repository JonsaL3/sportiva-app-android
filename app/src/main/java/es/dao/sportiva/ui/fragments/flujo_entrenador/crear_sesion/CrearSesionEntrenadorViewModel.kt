package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.repository.EntrenadorRepo
import es.dao.sportiva.repository.SesionRepo
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CrearSesionEntrenadorViewModel @Inject constructor(
    private var uiState: UiState,
    private val sesionRepo: SesionRepo,
    private val entrenadorRepo: EntrenadorRepo
) : ViewModel() {

    /**
     * Datos que recojo del formulario, la imagen la guardo como un base64 en forma de string
     */
    // Datos recogidos del paso 1 del formulario
    private val _entrenadoresParticipantes = MutableLiveData<EntrenadorWrapper>()
    val entrenadoresParticipantes = _entrenadoresParticipantes

    // Datos recogidos del paso 2 del formulario
    private val _tituloSesion = MutableLiveData("")
    val tituloSesion = _tituloSesion

    private val _subtituloSesion = MutableLiveData("")
    val subtituloSesion = _subtituloSesion

    private val _descripcionSesion = MutableLiveData("")
    val descripcionSesion = _descripcionSesion

    // Datos recogidos del paso 3 del formulario
    private val _fechaSesion = MutableLiveData<LocalDateTime>()
    val fechaSesion = _fechaSesion

    // Datos recogidos del paso 4 del formulario
    private val _imagenSesion = MutableLiveData<String?>(null)
    val imagenSesion = _imagenSesion

    // Datos recogidos del paso 5 del formulario
    private val _aforoSesion = MutableLiveData(0) // Constantes.AFORO_ILIMITADO
    val aforoSesion = _aforoSesion

    /**
     * Datos que necesito en el flujo de crear sesión
     */
    private val _entrenadoresDisponibles = MutableLiveData(EntrenadorWrapper())
    val entrenadoresDisponibles: LiveData<EntrenadorWrapper> = _entrenadoresDisponibles

    /**
     * Cargo los datos que necesito para poder mostrar el formulario de crear sesion
     */
    fun cargarDatosIniciales(idEmpresa: Int) = viewModelScope.launch {
        uiState.setLoading()
        entrenadorRepo.findEntrenadoresByIdEmpresa(idEmpresa)?.apply {
            _entrenadoresDisponibles.postValue(this)
            uiState.setSuccess()
        }
    }

    /**
     * Obtengo los datos del formulario y del creador de la sesión y creo la sesion para
     * enviarsela al servidor
     */
    fun crearSesion(creadorSesion: Entrenador) = viewModelScope.launch {
        uiState.setLoading()
        if (isAllowedToCreateSesion()) {
            val sesionCreada = Sesion(
                titulo = tituloSesion.value!!, // Nunca debería ser null si tod.o fue bien
                subtitulo = subtituloSesion.value!!,
                descripcion = descripcionSesion.value!!,
                fechaInserccion = Constantes.DEFAULT_DATE,
                fechaSesion = fechaSesion.value!!,
                aforoMaximo = aforoSesion.value!!,
                imagen = imagenSesion.value,
                empresa = creadorSesion.empresaAsignada,
                creador = creadorSesion,
                entrenadores = entrenadoresParticipantes.value!!
            )
            sesionRepo.crearSesion(sesionCreada)?.let {
                uiState.setSuccess()
            }
        }
    }

    /**
     * Añado a la lista de entrenadores seleccionados los entrenadores
     * proporcionados por una lista
     */
    fun addEntrenadoresParticipantes(entrenadores: EntrenadorWrapper) {
        entrenadores.forEach { it.isSeleccionadoParaSerParticipante =  true }
        _entrenadoresParticipantes.value!!.addAll(entrenadores)
        _entrenadoresParticipantes.value = _entrenadoresParticipantes.value
    }

    /**
     * Seteo desde fuera los datos de los entrenadores participantes
     */
    fun setTituloSession(titulo: String) {
        _tituloSesion.value = titulo
    }

    fun setSubtituloSession(subtitulo: String) {
        _subtituloSesion.value = subtitulo
    }

    fun setDescripcionSession(descripcion: String) {
        _descripcionSesion.value = descripcion
    }

    fun setFechaSession(fecha: LocalDateTime) {
        _fechaSesion.value = fecha
    }

    /**
     * Compruebo que los datos recogidos del formulario son correctos
     */
    private fun isAllowedToCreateSesion(): Boolean {
        // TODO MANDAR AL FICHERO STRING
        if (entrenadoresParticipantes.value.isNullOrEmpty()) {
            uiState.setError("Debes seleccionar al menos un entrenador")
            return false
        }
        if (tituloSesion.value.isNullOrEmpty()) {
            uiState.setError("Debes introducir un título para la sesión")
            return false
        }
        if (subtituloSesion.value.isNullOrEmpty()) {
            uiState.setError("Debes introducir un subtítulo para la sesión")
            return false
        }
        if (descripcionSesion.value.isNullOrEmpty()) {
            uiState.setError("Debes introducir una descripción para la sesión")
            return false
        }
        if (fechaSesion.value == Constantes.DEFAULT_DATE) {
            uiState.setError("Debes seleccionar una fecha para la sesión")
            return false
        }
        if (!imagenSesion.value.isNullOrEmpty()) {
            uiState.setError("Debes seleccionar una imagen para la sesión")
            return false
        }

        if (aforoSesion.value == 0) {
            uiState.setError("Debes introducir un aforo para la sesión")
            return false
        }
        return true
    }

}