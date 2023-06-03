package es.dao.sportiva.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Empresa(
    var id: Int = 0,
    var nombre: String = "",
    @SerializedName("activo")
    var isActivo: Boolean = false,
) : Serializable{
    //Hola mundo
}
