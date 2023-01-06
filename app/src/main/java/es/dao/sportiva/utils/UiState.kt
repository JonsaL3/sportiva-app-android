package es.dao.sportiva.utils

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Esta clase es un singleton instanciado a nivel de aplicacion usando DaggerHilt, lo que me permite inyectar esto en los viewmodels
 * y fragments / activitys que me de la gana que siempre va a ser la misma instancia siempre que se inyecte. Si se instancia llamando al constructor por
 * defecto, se crea una nueva instancia, y eso NO MOLA.
 */

@Singleton
class UiState @Inject constructor() {

    var uiState: MutableLiveData<State> = MutableLiveData(State.SUCCESS)
    // VAR MENSAJE =??¿?¿?¿? todo mandar mensaje de error desde la API

    fun setSuccess() {
        uiState.postValue(State.SUCCESS)
    }

    fun setLoading() {
        uiState.postValue(State.LOADING)
    }

    fun setError() {
        uiState.postValue(State.ERROR)
    }

    enum class State {
        LOADING, ERROR, SUCCESS
    }

}