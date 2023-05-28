package es.dao.sportiva.models.usuario

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RegistroRequest(
    @SerializedName("empleado")
    var isEmpleado: Boolean = false,
    var json: String = ""
) : Serializable