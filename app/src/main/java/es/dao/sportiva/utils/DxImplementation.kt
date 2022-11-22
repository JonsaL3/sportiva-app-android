package es.dao.sportiva.utils

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.dxcustomlibrary.DxCustom
import es.dao.sportiva.R

object DxImplementation {

    // Implementaciones primarias
    fun mostrarDxError(
        context: Context,
        mensaje: String
    ) {
        runOnUiThread {
            DxCustom(context)
                .createDialog(fullScreen = true)
                .setTitulo(context.getString(R.string.error))
                .setMensaje(mensaje)
                .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_error_outline_24))
                .noPermitirSalirSinBotones()
                .showAceptarButton { }
                .showDialogReturnDialog()
        }
    }

    fun mostrarDxWarning(
        context: Context,
        mensaje: String
    ) {

        runOnUiThread {
            DxCustom(context)
                .createDialog(fullScreen = true)
                .setTitulo(context.getString(R.string.atencion))
                .setMensaje(mensaje)
                .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
                .noPermitirSalirSinBotones()
                .showAceptarButton { }
                .showDialogReturnDialog()
        }
    }

    // Implementaciones secundarias
    fun mostrarDxErrorConexion(context: Context) {
        mostrarDxError(
            context = context,
            mensaje = context.getString(R.string.error_generico_conexion),
        )
    }

}