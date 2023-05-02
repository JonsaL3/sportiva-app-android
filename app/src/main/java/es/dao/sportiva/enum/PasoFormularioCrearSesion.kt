package es.dao.sportiva.enum

enum class PasoFormularioCrearSesion {

    SOLICITAR_ENTRENADORES,
    SOLICITAR_INFORMACION_GENERAL,
    SOLICITAR_FECHA_Y_HORA,
    SOLICITAR_AFORO,
    SOLICITAR_IMAGENES;

    companion object {
        fun fromInt(value: Int) = values().first { it.ordinal == value }
    }

}