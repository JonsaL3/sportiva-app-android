package es.dao.sportiva.models

import java.io.Serializable

data class Empresa(
    var id: Int = -1,
    var nombre: String = "",
    var isActivo: Boolean = false,
) : Serializable{
    //Hola mundo
}
