package es.dao.sportiva.ui

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ActivityMainBinding
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    var actionOnBackPressed: (() -> Unit)? = null

    private lateinit var navHostFragment: NavHostFragment

    private lateinit var navController: NavController

    // Inyecto el singleton que maneja los estados de la UI, por lo que desde aqui puedo observar cambios
    // que realize desde cualquier repo / viewmodel / servicio / etc en el cual inyecte esta misma clase
    @Inject lateinit var uiState: UiState

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
        setupObservers()
        setupDrawer()
        setupNavigationListener()
        viewModel.downloadNewVersionIfAvaiable(this) {
            binding.llComprobandoVersiones.visibility = View.GONE
        }
        setupListeners()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }

    private fun setupObservers() {
        setupUiStates()
        observeUsuario()
    }

    private fun setupNavigationListener() {

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {

                R.id.welcomeFragment -> { // TODO al ponerlo en gone se oculta el loader porque está dentro.
                    binding.appBarLayout.visibility = View.GONE
                }

                R.id.entrenadorMainFragment -> {
                    // set label
                    binding.topAppBar.title = getString(R.string.seleccionar_accion) // TODO ESTO PUEDE HACERSE AUTOMATICAMENTE
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
                    // TODO
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

            }

        }

    }

    private fun setupUiStates() {

        uiState.observableState.observe(this) { state ->

            when (state) {
                UiState.State.LOADING -> {
                    binding.piMainActivity.visibility = View.VISIBLE
                }
                UiState.State.SUCCESS -> {
                    binding.piMainActivity.visibility = View.GONE
                }
                UiState.State.ERROR -> {
//
                    binding.piMainActivity.visibility = View.GONE

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

    private fun setupListeners() = with(binding){
        mcvMostrarQrDrawer.setOnClickListener {
            mostrarCodigoQr()
        }
        mcvMostrarCondiguracionDrawer.setOnClickListener {
            //TODO
        }
        mcvMostrarSalirDrawer.setOnClickListener {
            drawerLayout.close()
            navController.navigateUp()
        }
    }

    private fun mostrarCodigoQr() {
        DxImplementation.mostarDxGeneararBarcode(
            context = this,
            barcode =  viewModel.usuario.value?.id.toString() + " ; " + LocalDate.now() + " ; " + viewModel.usuario.value?.javaClass?.simpleName,
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

    }

}