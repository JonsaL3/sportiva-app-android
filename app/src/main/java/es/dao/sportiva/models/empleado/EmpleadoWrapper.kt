package es.dao.sportiva.models.empleado

class EmpleadoWrapper : ArrayList<Empleado> {

    constructor() : super()

    constructor(arrayList: ArrayList<Empleado>) : this() {
        this.addAll(arrayList)
    }

}