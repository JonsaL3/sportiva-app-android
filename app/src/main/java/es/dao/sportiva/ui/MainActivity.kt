package es.dao.sportiva.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ActivityMainBinding
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        setContentView(binding.root)
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
    }

    private fun setupObservers() {
        observeUsuario()
        setupUiStates()
    }


    private fun setupUiStates() {
//        uiState.observableState.observe(this) { state ->
//            when (state) {
//                UiState.State.LOADING -> {
//                    binding.piMainActivity.show()
//                }
//                UiState.State.SUCCESS -> {
//                    binding.piMainActivity.hide()
//                }
//                UiState.State.ERROR -> {
//                    binding.piMainActivity.hide()
//                    if (uiState.errorMessage.isNotEmpty()) {
//                        DxImplementation.mostrarDxWarning(this, uiState.errorMessage)
//                    } else {
//                        DxImplementation.mostrarDxError(this, "Error desconocido")
//                    }
//                }
//                null -> {}
//            }
//        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeUsuario() {

//        viewModel.usuario.observe(this) { usuario ->
//
//            usuario?.let {
//
//                val campoNombre = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_nombre_usuario)
//                campoNombre.text = "${usuario.nombre} ${usuario.apellido1} ${usuario.apellido2}"
//
//                when (it) {
//                    is Empleado -> {
//                        binding.navView.menu.clear()
//                        binding.navView.inflateMenu(R.menu.menu_drawer_flujo_empleado)
//                        setupActionsNavViewEmpleado()
//                    }
//                    else -> {
//                        binding.navView.menu.clear()
//                        binding.navView.inflateMenu(R.menu.menu_drawer_flujo_entrenador)
//                        setupActionsNavViewEntrenador()
//                    }
//                }
//
//            } ?: run {
//
//            }
//
//        }

    }

    private fun setupActionsNavViewEmpleado() {
//        binding.navView.setNavigationItemSelectedListener { menuItem ->
//            when(menuItem.itemId) {
//                R.id.nav_modificar_perfil_empleado -> {
//                    true
//                }
//                R.id.nav_ver_codigo_qr_empleado -> {
//                    mostrarCodigoQr()
//                    true
//                }
//                else -> false
//            }
//        }
    }

    private fun setupActionsNavViewEntrenador() {
//        binding.navView.setNavigationItemSelectedListener { menuItem ->
//            when(menuItem.itemId) {
//                R.id.nav_modificar_perfil_entrenador -> {
//                    true
//                }
//                else -> false
//            }
//        }
    }

    private fun mostrarCodigoQr() {

        DxImplementation.mostarDxGeneararBarcode(
            context = this,
            barcode =  viewModel.usuario.value?.id.toString() + " ; " + LocalDate.now() + " ; " + viewModel.usuario.value?.javaClass?.simpleName,
        )
    }

    private fun setupDrawer() {

//        // Apertura y cierre del drawer con la appbar
//        val drawer = binding.drawerLayout
//        val toggle = ActionBarDrawerToggle(
//            this,
//            drawer,
//            binding.appbar,
//            R.string.abierto,
//            R.string.cerrado
//        )
//        drawer.addDrawerListener(toggle)
//        toggle.syncState()

    }

}