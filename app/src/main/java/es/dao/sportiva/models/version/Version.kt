package es.dao.sportiva.models.version

import com.google.gson.annotations.SerializedName

data class Version(
    @SerializedName("versionInt")
    var versionInt: Int = 0
)