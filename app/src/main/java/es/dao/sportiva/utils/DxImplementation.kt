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
import es.dao.sportiva.databinding.*
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.ui.adapters.SeleccionarEntrenadoresRecyclerViewAdapter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        mensaje: String,
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
            .setIcono(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_baseline_warning_amber_24
                )
            ) // TODO QUIZAS CAMBIAR ICONO
            .noPermitirSalirSinBotones()
            .showAceptarButton { onAccept?.invoke() }
            .showCancelarButton { }
            .showDialogReturnDialog()
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
        entrenadoresEnMiMismaEmpresa: EntrenadorWrapper,
        entrenadoresYaEnLaLista: EntrenadorWrapper,
        onEntrenadoresSelected: (EntrenadorWrapper) -> Unit
    ) = runOnUiThread {

        val entrenadoresSeleccionados = EntrenadorWrapper()

        val entrenadoresSeleccionables = entrenadoresEnMiMismaEmpresa.filter { entrenador ->
            !entrenadoresYaEnLaLista.any { it.id == entrenador.id }
        }

        if (entrenadoresSeleccionables.isEmpty()) {
            mostrarDxError(
                context = context,
                mensaje = "No hay mas entrenadores disponibles para seleccionar asignados a la misma empresa que tu."
            )
        } else {

            val customLayoutBinding = DxListaEntrenadoresParticipantesBinding.inflate(LayoutInflater.from(context))

            DxCustom(context)
                .createDialog(fullScreen = true)
                .addCustomView(customLayoutBinding.root)
                .setTitulo("Seleccionar entrenadores.")
                .setMensaje("Seleccione los entrenadores disponibles asignados a la misma empresa que tu que participarán en la sesión.")
                .setIcono(ContextCompat.getDrawable(context, R.drawable.baseline_edit_24))
                .noPermitirSalirSinBotones()
                .showAceptarButton {
                    onEntrenadoresSelected(entrenadoresSeleccionados)
                }
                .showCancelarButton { }
                .showDialogReturnDxCustom()

            val onCheckedUsuario = { entrenador: Entrenador, isChecked: Boolean ->
                entrenador.isSeleccionadoParaSerParticipante = isChecked
                if (isChecked)
                    entrenadoresSeleccionados.add(entrenador)
                else
                    entrenadoresSeleccionados.remove(entrenador)
                Unit
            }

            val adapter = SeleccionarEntrenadoresRecyclerViewAdapter(onCheckedUsuario)
            customLayoutBinding.rvEntrenadoresParticipantes.adapter = adapter
            adapter.submitList(entrenadoresSeleccionables)

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

    fun mostrarDxConfirmarCrearSesion(
        context: Context,
        entrenadoresParticipantes: EntrenadorWrapper,
        titulo: String,
        fechaYHora: LocalDateTime,
        aforo: Int,
        onAccept: () -> Unit
    ) = runOnUiThread {

        val nombresEntrenadores = entrenadoresParticipantes.joinToString(", ") { it.nombre }
        val mAforo = if (aforo == Constantes.AFORO_ILIMITADO) "Ilimitado" else aforo.toString()
        val resumenSesionACrear = "Estas a punto de crear una sesión con los siguientes datos:<br><br>" +
                "Título: $titulo<br>" +
                "Fecha y hora: ${fechaYHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))}<br>" +
                "Aforo: $mAforo<br>" +
                "Entrenadores participantes: $nombresEntrenadores<br><br>" +
                "¿Estás seguro de que quieres crear la sesión?"

        DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo("Crear sesión")
            .setMensaje(resumenSesionACrear)
            .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
            .noPermitirSalirSinBotones()
            .showAceptarButton {
                onAccept.invoke()
            }
            .showCancelarButton { }
            .showDialogReturnDialog()

    }

    fun mostrarDxLottie(
        context: Context,
        titulo: String,
        mensaje: String,
        lottie: Int,
        onAccept: () -> Unit
    ) = runOnUiThread {

        val binding = DxMostrarLottieBinding.inflate(LayoutInflater.from(context))

        binding.lottieAnimationView.setAnimation(lottie)

        DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo(titulo)
            .setMensaje(mensaje)
            .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
            .noPermitirSalirSinBotones()
            .addCustomView(binding.root)
            .showAceptarButton {
                onAccept.invoke()
            }
            .showDialogReturnDialog()

    }

    fun mostrarDxLectorQr(
        context: Context,
        onQrScanned: (String) -> Unit
    ) = runOnUiThread {

        val binding = DxLectorQrBinding.inflate(LayoutInflater.from(context))

        val dx = DxCustom(context)
            .createDialog(fullScreen = true)
            .setTitulo(context.getString(R.string.atencion))
            .setMensaje(context.getString(R.string.escanea_qr))
            .addCustomView(binding.root)
            .setIcono(ContextCompat.getDrawable(context, R.drawable.ic_baseline_warning_amber_24))
            .noPermitirSalirSinBotones()
            .showCancelarButton {
                binding.bvBarcodeBarcodeview.pause()
            }
            .showDialogReturnDialog()

        binding.bvBarcodeBarcodeview.apply {

            decodeContinuous {
                pause()
                onQrScanned(it.text)
                dx.hide()
            }

            resume()

        }

    }

}