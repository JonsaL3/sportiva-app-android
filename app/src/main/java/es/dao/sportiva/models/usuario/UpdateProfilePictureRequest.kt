package es.dao.sportiva.models.usuario

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdateProfilePictureRequest(
    @SerializedName("entrenador")
    var isEntrenador: Boolean = false,
    var userId: Int = 0,
    var base64: String = ""
) : Serializable