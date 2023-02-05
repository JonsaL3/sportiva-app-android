package es.dao.sportiva.utils

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ActivityCamaraBinding


class CamaraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCamaraBinding
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                // TODO VOLVER A SPORTIVA
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Arranco la cámara nada mas entrar
        startCamera()
    }

    // ############################################################################################
    // CONFIGURACIÓN DE LA CÁMARA #################################################################
    // ############################################################################################

    /**
     * Emparejo la cámara con el PreviewView
     */
    private fun bindPreview(cameraProvider : ProcessCameraProvider) {

        val cameraSelector = CameraSelector
            .Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val preview = Preview
            .Builder()
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        binding.previewView.viewPort?.let {
            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(preview)
                .setViewPort(it)
                .build()

            cameraProvider.unbindAll()
            val camera: Camera = cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)
            val cameraControl: CameraControl = camera.cameraControl
            cameraControl.setLinearZoom(0.3.toFloat())
        }
    }

    // ############################################################################################
    // CONTROL DE LA CÁMARA #######################################################################
    // ############################################################################################

    /**
     * Muestra en pantalla lo que ve la cámara del teléfono
     */
    private fun showPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    /**
     * Activo la cámara en caso de que tenga permisos, en caso contrario pido permisos
     */
    private fun startCamera() {
        if (hasCameraPermission()) {
            showPreview()
        } else {
            solicitarPermisos()
        }
    }

    // ############################################################################################
    // GESTIÓN DE PERMISOS ########################################################################
    // ############################################################################################

    /**
     * Solicito los permisos de la cámara
     */
    private fun solicitarPermisos() {
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    /**
     * Compruebo que los permisos de la camara estén aceptados
     */
    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

}