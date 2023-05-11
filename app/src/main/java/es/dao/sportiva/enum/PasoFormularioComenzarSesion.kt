package es.dao.sportiva.enum

enum class PasoFormularioComenzarSesion {

    SELECCIONAR_SESION,
    CONFIRMAR_ASISTENCIA;

    companion object {
        fun fromInt(value: Int) = values().first { it.ordinal == value }
    }

}