package es.dao.sportiva.models.entrenador

class EntrenadorWrapper(entrenadores: List<Entrenador>) : ArrayList<Entrenador>() {
    init {
        clear()
        addAll(entrenadores)
    }

    constructor() : this(listOf())

}