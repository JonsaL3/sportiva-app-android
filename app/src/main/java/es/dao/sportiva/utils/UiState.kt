package es.dao.sportiva.utils

import androidx.lifecycle.MutableLiveData
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Esta clase es un singleton instanciado a nivel de aplicacion usando DaggerHilt, lo que me permite inyectar esto en los viewmodels
 * y fragments / activitys que me de la gana que siempre va a ser la misma instancia siempre que se inyecte. Si se instancia llamando al constructor por
 * defecto, se crea una nueva instancia, y eso NO MOLA.
 *
 * De normal, estos estados se manejan de manera común en los viewmodels.
 * Pero estos casos (exito, error, ladando) son estados que se repiten en muchos viewmodels, y que se pueden manejar de manera común.
 *
 * Luego, aparte, cada viewmodel tiene sus estados concretos.
 */

@Singleton
class UiState @Inject constructor() {

    var observableState: MutableLiveData<State> = MutableLiveData(State.SUCCESS)
    var errorMessage = ""

    fun setSuccess() {
        observableState.postValue(State.SUCCESS)
    }

    fun setLoading() {
        observableState.postValue(State.LOADING)
    }

    fun setError(errorMessage: String) {
        this.errorMessage = errorMessage
        observableState.postValue(State.ERROR)
    }

    enum class State {
        LOADING, ERROR, SUCCESS
    }

}