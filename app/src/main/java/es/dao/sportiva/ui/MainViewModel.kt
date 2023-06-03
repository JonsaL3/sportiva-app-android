package es.dao.sportiva.ui

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dao.sportiva.BuildConfig
import es.dao.sportiva.R
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.usuario.Usuario
import es.dao.sportiva.utils.runOnUiThread
import es.dao.sportiva.models.usuario.IniciarSesionRequest
import es.dao.sportiva.models.usuario.UpdateProfilePictureRequest
import es.dao.sportiva.repository.UsuarioRepo
import es.dao.sportiva.repository.VersionRepo
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.utils.UiState
import es.dao.sportiva.utils.download_tools.AndroidDownloader
import es.dao.sportiva.utils.getFile
import es.dao.sportiva.utils.toBase64
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usuarioRepo: UsuarioRepo,
    private val versionRepo: VersionRepo,
    private val uiState: UiState
) : ViewModel() {

    /**
     * Variables de la sesión
     */
    private var _usuario: MutableLiveData<Usuario?> = MutableLiveData(null)
    val usuario: LiveData<Usuario?> = _usuario

    /**
     * Manejo de la sesión
     */
    fun doLogin(
        correo: String,
        contrasena: String,
        context: Context,
        onSuccess: (usuario: Usuario) -> Unit,
    ) = viewModelScope.launch(Dispatchers.IO) {

        uiState.setLoading()

        usuarioRepo.iniciarSesion(correo, contrasena)?.apply {
            Log.w(";;;", "usuario existente en viewmodel")
            runOnUiThread {
                uiState.setSuccess()
                _usuario.value = this
                onSuccess.invoke(this)
            }
        } ?: run {
            Log.w(";;;", "usuario nulo en viewmodel")
        }

    }

    fun doLogout() {
        _usuario.postValue(null)
    }

    /**
     * Manejo de la descarga de nuevas versiones de la aplicación
     */
    fun downloadNewVersionIfAvaiable(
        context: Context,
        noNewVersionAction : () -> Unit
        // Actualmente crearía un estado para esto pero este
        // proyecto lo empecé cuando no sabia de la existencia de nada de eso
        // y esta main activity es lo primero que cree
    ) = viewModelScope.launch {

        delay(500)

        versionRepo.getLatestVersion()?.apply {
            if (this.versionInt > BuildConfig.VERSION_CODE) {
                uiState.setSuccess()
                uiState.setLoadingFullScreen(
                    context.getString(R.string.descargando_nueva_versi_n_de_la_aplicaci_n),
                    context.getString(R.string.por_favor_espere)
                )
                downloadNewApk(context)
                return@launch
            } else {
                noNewVersionAction.invoke()
            }
        }?:run{
            noNewVersionAction.invoke()
        }

    }
    private fun downloadNewApk(context: Context) {

        // Antes de descargarme una nueva versión borro los apks por si hay alguno de antes
        val downloadDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path)
        downloadDir.listFiles()?.forEach {
            Log.d(":::DownloadCompletedReciverPRE", it.name)
            if (it.name.contains("sportiva")) {
                if (it.delete()) {
                    Log.d(":::DownloadCompletedReciverPRE", "Borrado apk viejo: ${it.name}")
                    MediaScannerConnection.scanFile(
                        context, arrayOf(it.path), null, null
                    )
                }
                else
                    Log.e(":::DownloadCompletedReciverPRE", "No se pudo borrar apk viejo: ${it.name}")
            }
        }

        val downloader = AndroidDownloader(context)
        downloader.donwloadApkFile(Constantes.BASE_URL + "version/downloadCurrentApk")
    }
    
    fun updatePrifilePicture(context: Context, data: String, onSuccess: () -> Unit){
        viewModelScope.launch {
            try{
                val uriImagen = Uri.parse(data)
                val file = uriImagen.getFile(context)
                val compressedFile = Compressor.compress(context, file) {
                    resolution(1280, 720)
                    quality(50)
                    format(Bitmap.CompressFormat.JPEG)
                }
                
                if(usuarioRepo.updateProfilePicture(
                        UpdateProfilePictureRequest(
                            usuario.value is Entrenador,
                            _usuario.value?.id ?: 0,
                            compressedFile.toUri().toBase64(context)?:""
                        )
                    ) == true){
                    onSuccess.invoke()
                }else{
                    uiState.setError(context.getString(R.string.no_se_pudo_actualizar_la_imagen))
                }
                
            }catch (e: Exception) {
                e.printStackTrace()
                uiState.setError(context.getString(R.string.no_se_pudo_actualizar_la_imagen))
            }
        }
    }

}