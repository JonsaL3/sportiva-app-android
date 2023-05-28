package es.dao.sportiva.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.MainNavGraphXmlDirections
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ActivityMainBinding
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.ui.fragments.login.WelcomeFragmentDirections
import es.dao.sportiva.utils.CamaraActivity
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import es.dao.sportiva.utils.fromBase64
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    var actionOnBackPressed: (() -> Unit)? = null

    private lateinit var navHostFragment: NavHostFragment

    // Inyecto el singleton que maneja los estados de la UI, por lo que desde aqui puedo observar cambios
    // que realize desde cualquier repo / viewmodel / servicio / etc en el cual inyecte esta misma clase
    @Inject lateinit var uiState: UiState

    private val realizarFotografiaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {

            val onSuccsess: () -> Unit = {
                Glide.with(this).load(result.data?.data.toString()).circleCrop().into(binding.ivFotoPerfil)
                binding.tvInicial.visibility = View.GONE
            }

            viewModel.updatePrifilePicture(this, result.data?.data.toString(), onSuccsess)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        setupView()

        //registar a onbackpressed callback
        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                actionOnBackPressed?.invoke()?: navHostFragment.navController.navigateUp()
            }
        })
    }

    private fun setupView() {

        uiState.setLoadingFullScreen(
            getString(R.string.comprobando_versiones),
            getString(R.string.estamos_comprobando_si_hay_nuevas_versiones_disponibles),
        )

        setupObservers()
        setupDrawer()
        setupNavigationListener()
        viewModel.downloadNewVersionIfAvaiable(this) {
            DxImplementation.quitarLoader()
        }
    }

    private fun setupObservers() {
        setupUiStates()
        observeUsuario()
    }

    private fun setupNavigationListener() {

        val navController = navHostFragment.findNavController()
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.welcomeFragment -> {
                    binding.appBarLayout.visibility = View.GONE
                    viewModel.doLogout()
                }

                R.id.entrenadorMainFragment -> {
                    binding.topAppBar.title = getString(R.string.seleccionar_accion)
                    binding.appBarLayout.visibility = View.VISIBLE
                }

                R.id.crearSesionFragment -> {
                    binding.topAppBar.title = getString(R.string.crear_sesion)
                    binding.appBarLayout.visibility = View.VISIBLE
                }

                R.id.comenzarSesionFragment -> {
                    binding.topAppBar.title = getString(R.string.comenzar_sesion)
                    binding.appBarLayout.visibility = View.VISIBLE
                }

                R.id.empleadoMainFragment -> {
                    binding.topAppBar.title = getString(R.string.apuntarse_a_sesion)
                    binding.appBarLayout.visibility = View.VISIBLE
                }

            }

        }

    }

    private fun observeUsuario() {

        viewModel.usuario.observe(this) { usuario ->

            usuario?.let { mUsuario ->

                binding.tvNombreUsuario.text = mUsuario.nombre + " " + mUsuario.apellido1 + " " + mUsuario.apellido2

                mUsuario.imagen.takeIf { !it.isNullOrBlank() }?.let { imagen ->
                    binding.ivFotoPerfil.visibility = View.VISIBLE
                    Glide.with(this).load(imagen.fromBase64()).circleCrop().into(binding.ivFotoPerfil)
                    binding.tvInicial.visibility = View.GONE
                } ?: run {
                    binding.flNoHayFoto.visibility = View.VISIBLE
                    binding.tvInicial.text = mUsuario.nombre.firstOrNull()?.toString() ?: "-"
                }


                try {
                    mUsuario as Entrenador
                    binding.tvDescripcionUsuario.text =
                        "Entrenador en ${mUsuario.empresaAsignada.nombre}\n${mUsuario.estudios}"
                } catch (e: ClassCastException) {
                    mUsuario as Empleado
                    binding.tvDescripcionUsuario.text =
                        "Empleado en ${mUsuario.empresa.nombre}\n${mUsuario.cargo}"
                    binding.mcvMostrarQrDrawer.visibility = View.VISIBLE
                }

            } ?: run {
                binding.mcvMostrarQrDrawer.visibility = View.GONE
            }

        }

    }

    private fun setupUiStates() {

        uiState.observableState.observe(this) { state ->

            when (state) {
                UiState.State.LOADING -> {
                    binding.piMainActivity.visibility = View.VISIBLE
                }
                UiState.State.LOADING_FULL_SCREEN -> {
                    DxImplementation.mostrarLoader(
                        this,
                        uiState.tituloLoader,
                        uiState.mensajeLoader
                    )
                }
                UiState.State.SUCCESS -> {
                    binding.piMainActivity.visibility = View.GONE
                    DxImplementation.quitarLoader()
                }
                UiState.State.ERROR -> {
//
                    binding.piMainActivity.visibility = View.GONE
                    DxImplementation.quitarLoader()

                    if (uiState.errorMessage.isNotEmpty()) {
                        DxImplementation.mostrarDxWarning(this, uiState.errorMessage)
                    } else {
                        DxImplementation.mostrarDxError(this, getString(R.string.error_desconocido))
                    }
                }
                else -> {}
            }
        }

    }

    private fun mostrarCodigoQr() {
        binding.drawerLayout.close()
        DxImplementation.mostarDxGeneararBarcode(
            context = this,
            barcode =  (viewModel.usuario.value as Empleado?)?.getStringForQR() ?: "Error al generar el código QR"
        )
    }

    private fun setupDrawer() {

        // Apertura y cierre del drawer con la appbar
        val drawer = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            binding.topAppBar,
            R.string.abierto,
            R.string.cerrado
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        binding.mcvConfiguracion.visibility = View.GONE

        binding.ivFotoPerfil.setOnClickListener {
            realizarImagen()
        }

        binding.mcvMostrarQrDrawer.setOnClickListener {
            mostrarCodigoQr()
        }

        binding.mcvSalir.setOnClickListener {
            binding.drawerLayout.close()
            viewModel.doLogout()
            val action =
                MainNavGraphXmlDirections.actionGlobalWelcomeFragment()
            navHostFragment.navController.navigate(action)
        }

    }

    private fun realizarImagen() {
        val intent = Intent(this, CamaraActivity::class.java)
        realizarFotografiaResult.launch(intent)
    }


}