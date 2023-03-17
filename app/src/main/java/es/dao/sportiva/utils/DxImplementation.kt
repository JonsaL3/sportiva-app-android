package es.dao.sportiva.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.dxcustomlibrary.DxCustom
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import es.dao.sportiva.R
import es.dao.sportiva.databinding.DxLectorQrBinding
import es.dao.sportiva.databinding.DxListaEntrenadoresParticipantesBinding
import es.dao.sportiva.databinding.DxListaSesionesBinding
import es.dao.sportiva.databinding.DxMostrarQrBinding
import es.dao.sportiva.databinding.LoginLayoutBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.ui.adapters.EntrenadoresParticipantesRecyclerViewAdapter
import es.dao.sportiva.ui.adapters.EntrenadoresParticipantesViewHolder
import es.dao.sportiva.ui.adapters.SeleccionarEntrenadoresRecyclerViewAdapter
import es.dao.sportiva.ui.adapters.SeleccionarSesionRecyclerViewAdapter

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

    fun mostrarDxConfirmacion(
        context: Context,
        titulo: String,
        mensaje: String,
        onAccept: (() -> Unit)? = null,
    ) = runOnUiThread {

        DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo(titulo)
            .setMensaje(mensaje)
            .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24)) // TODO QUIZAS CAMBIAR ICONO
            .noPermitirSalirSinBotones()
            .showAceptarButton { onAccept?.invoke() }
            .showCancelarButton {  }
            .showDialogReturnDialog()
    }

    fun mostrarDxDescartarCambios(
        context: Context,
        mensaje: String
    ) = runOnUiThread {

        DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo(context.getString(R.string.atencion))
            .setMensaje(mensaje)
            .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
            .noPermitirSalirSinBotones()
            .showAceptarButton { }
            .showDialogReturnDialog()

    }

    fun mostrarDxListaSesionesSeleccionar(
        context: Context,
        sesiones: List<Sesion>?,
        onSessionSelected: (Sesion) -> Unit
    ) = runOnUiThread {

        sesiones?.let { listaSesiones ->

            val customLayoutBinding = DxListaSesionesBinding.inflate(LayoutInflater.from(context))

            val dx = DxCustom(context)
                .createDialog(fullScreen = true)
                .addCustomView(customLayoutBinding.root)
                .setTitulo(context.getString(R.string.seleccione_sesion))
                .setMensaje(context.getString(R.string.seleccione_sesion_a_comenzar))
                .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
                .noPermitirSalirSinBotones()
                .showCancelarButton { }
                .showDialogReturnDxCustom()

            val onSessionSelectedRecycler = { sesion: Sesion ->
                onSessionSelected.invoke(sesion)
                dx.hideDialog()
            }
            val adapter = SeleccionarSesionRecyclerViewAdapter(onSessionSelectedRecycler)
            customLayoutBinding.rvSesiones.adapter = adapter
            adapter.submitList(listaSesiones)

        } ?: run {

            mostrarDxError(
                context = context,
                mensaje = context.getString(R.string.no_se_han_creado_sesiones)
            )

        }

    }

    fun mostrarDxLectorQr(
        context: Context,
        onQrScanned: (String) -> Unit
    ) = runOnUiThread {

        val binding = DxLectorQrBinding.inflate(LayoutInflater.from(context))

        DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo(context.getString(R.string.atencion))
            .setMensaje(context.getString(R.string.escanea_qr))
            .addCustomView(binding.root)
            .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
            .noPermitirSalirSinBotones()
            .showCancelarButton { }
            .showDialogReturnDialog()

        // TODO ESCANEAR EL BARCODE
    }

    fun mostarDxGeneararBarcode(
        context: Context,
        barcode: String,
    ) {

        val binding = DxMostrarQrBinding.inflate(LayoutInflater.from(context))

        DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo(context.getString(R.string.qr))
            .setMensaje(context.getString(R.string.este_qr_es_unico))
            .addCustomView(binding.root)
            .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_qr_code_24))
            .noPermitirSalirSinBotones()
            .showAceptarButton { }
            .showDialogReturnDialog()


        val qrGenerator = QRCodeWriter()
        val bitMatrix = qrGenerator.encode(barcode, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        binding.sivBarcode.setImageBitmap(bmp)

    }

    fun mostrarDxSeleccionarEntrenador(
        context: Context,
        entrenadores: ArrayList<Entrenador>?,
        idCreador: Int,
        onEntrenadoresSelected: (EntrenadorWrapper) -> Unit
    ) = runOnUiThread {

        entrenadores?.let { entrenadores ->

            val customLayoutBinding = DxListaEntrenadoresParticipantesBinding.inflate(LayoutInflater.from(context))

            val dx = DxCustom(context)
                .createDialog(fullScreen = true)
                .addCustomView(customLayoutBinding.root)
                .setTitulo("Seleccionar entrenadores.")
                .setMensaje("Seleccione los entrenadores disponibles asignados a la misma empresa que tu que participarán en la sesión.")
                .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
                .noPermitirSalirSinBotones()
                .showAceptarButton { onEntrenadoresSelected.invoke(EntrenadorWrapper(entrenadores.filter { !it.isSeleccionadoParaSerParticipante })) }
                .showCancelarButton { }
                .showDialogReturnDxCustom()

            val adapter = SeleccionarEntrenadoresRecyclerViewAdapter()
            customLayoutBinding.rvEntrenadoresParticipantes.adapter = adapter
            entrenadores.removeIf { it.id == idCreador || it.isSeleccionadoParaSerParticipante }
            adapter.submitList(entrenadores)

        } ?: run {

            mostrarDxError(
                context = context,
                mensaje = "No hay mas entrenadores asignados a la misma empresa que tu."
            )

        }

    }

    fun mostrarDxLogin(context: Context, actionOnAccept: (correo: String, contrasena: String) -> Unit) {

        val binding = LoginLayoutBinding.inflate(LayoutInflater.from(context))

        binding.etCorreoElectronico.setText("david@david.es")
        binding.etContrasena.setText("1234")

        DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo(context.getString(R.string.iniciar_sesi_n))
            .setMensaje(context.getString(R.string.nos_alegramos_de_verte))
            .setIcono(ContextCompat.getDrawable(context, R.drawable.confetti_svgrepo_com), ContextCompat.getColor(context, R.color.red_sportiva))
            .noPermitirSalirSinBotones()
            .addCustomView(binding.root)
            .showAceptarButton(
                color = ContextCompat.getColor(context, R.color.red_sportiva),
            ) {
                actionOnAccept.invoke(binding.etCorreoElectronico.text.toString(),binding.etContrasena.text.toString())
            }
            .showCancelarButton(
                texto = context.getString(R.string.volver_atras),
                strokecolor = ContextCompat.getColor(context, R.color.red_sportiva)
            ) {  }
            .showDialogReturnDialog()
    }

}