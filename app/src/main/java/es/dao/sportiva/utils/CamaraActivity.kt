package es.dao.sportiva.utils

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.common.util.concurrent.ListenableFuture
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ActivityCamaraBinding
import java.io.File
import java.time.LocalDateTime


class CamaraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCamaraBinding
    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>

    private lateinit var camera: Camera
    private lateinit var imageCapture: ImageCapture


    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamaraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Arranco la cámara nada mas entrar
        startCamera()
        
        // El listener para tomar la imagen.
        binding.btnTomarImagen.setOnClickListener {
            takePhoto()
        }
        
        // el listener para cerrar la camara
        binding.btnVolverAtras.setOnClickListener {
            finish()
        }
        
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

        imageCapture = ImageCapture.Builder()
            .setTargetRotation(binding.root.display.rotation)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        binding.previewView.viewPort?.let {

            val useCaseGroup = UseCaseGroup.Builder()
                .addUseCase(preview)
                .addUseCase(imageCapture)
                .setViewPort(it)
                .build()

            cameraProvider.unbindAll()

            // Instancia de camera
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)

            // Instancia del control de la camara
            camera.cameraControl.setLinearZoom(0.3.toFloat())
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

    /**
     * Realizo una fotografía, obtengo la uri y la devuelvo como activity result
     */
    private fun takePhoto() {
        val outputFileOptions = ImageCapture.OutputFileOptions
            .Builder(File("${this.externalCacheDir}${File.separator}${System.currentTimeMillis()}.png"))
            .build()

        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this),

            object : ImageCapture.OnImageSavedCallback {
                override fun onError(error: ImageCaptureException) {
                    error.printStackTrace()
                }
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val data = Intent()
                    data.data = outputFileResults.savedUri
                    setResult(RESULT_OK, data)
                    finish()
                }
            }

        )
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