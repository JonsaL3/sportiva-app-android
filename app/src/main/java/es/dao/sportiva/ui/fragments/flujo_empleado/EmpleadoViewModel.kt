package es.dao.sportiva.ui.fragments.flujo_empleado

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.models.sesion.SesionWrapper
import es.dao.sportiva.webservice.entrenador.EntrenadorRepo
import es.dao.sportiva.webservice.sesion.SesionRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EmpleadoViewModel : ViewModel() {

    private var _sesionesDisponibles: MutableLiveData<SesionWrapper?> = MutableLiveData(null)
    val sesionesDisponibles: LiveData<SesionWrapper?> = _sesionesDisponibles

    private var _entrenadores: MutableLiveData<EntrenadorWrapper?> = MutableLiveData(null)
    val entrenadores: LiveData<EntrenadorWrapper?> = _entrenadores

    fun obtenerDatos(
        idEmpresa: Int,
        context: Context? = null
    ) {

        viewModelScope.launch {

            val job = CoroutineScope(Dispatchers.IO).launch {

                val sesiones = SesionRepo.findSesionesDisponibles(
                    idEmpresa = idEmpresa,
                    context = context
                )
                _sesionesDisponibles.postValue(sesiones)

                val entrenadores = EntrenadorRepo.findEntrenadoresByIdEmpresa(
                    idEmpresa = idEmpresa,
                    context = context
                )
                _entrenadores.postValue(entrenadores)
            }

            job.join()

            // TODO AQUI TERMINAR ANIMACION DE CARGA

        }

    }

    // Manejo de las sesiones disponibles ##########################################
    private suspend fun findSesionesDisponibles(
        idEmpresa: Int,
        context: Context? = null
    ): SesionWrapper? = SesionRepo.findSesionesDisponibles(
        idEmpresa = idEmpresa,
        context = context
    )

    // Manejo de los entrenadores asignados a la empresa ##################################################
    private suspend fun findEntrenadoresByIdEmpresa(
        idEmpresa: Int,
        context: Context? = null
    ): EntrenadorWrapper? = EntrenadorRepo.findEntrenadoresByIdEmpresa(
        idEmpresa = idEmpresa,
        context = context
    )

}