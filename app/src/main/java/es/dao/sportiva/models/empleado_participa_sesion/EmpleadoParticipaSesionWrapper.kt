package es.dao.sportiva.models.empleado_participa_sesion

class EmpleadoParticipaSesionWrapper : ArrayList<EmpleadoParticipaSesion> {

    constructor() : super()

    constructor(arrayList: ArrayList<EmpleadoParticipaSesion>) : this() {
        this.addAll(arrayList)
    }

}