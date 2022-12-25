package es.dao.sportiva.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ActivityMainBinding
import es.dao.sportiva.databinding.DrawerHeaderLayoutBinding
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.ui.fragments.MainViewModel
import es.dao.sportiva.utils.DxImplementation
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
        setupView()
    }

    private fun setupView() {
        setupObservers()
        setupDrawer()
    }

    private fun setupObservers() {
        observeUsuario()
    }

    @SuppressLint("SetTextI18n")
    private fun observeUsuario() {

        viewModel.usuario.observe(this) { usuario ->

            usuario?.let {

                val campoNombre = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_nombre_usuario)
                campoNombre.text = "${usuario.nombre} ${usuario.apellido1} ${usuario.apellido2}"

                when (it) {
                    is Empleado -> {
                        binding.navView.menu.clear()
                        binding.navView.inflateMenu(R.menu.menu_drawer_flujo_empleado)
                        setupActionsNavViewEmpleado()
                    }
                    else -> {
                        binding.navView.menu.clear()
                        binding.navView.inflateMenu(R.menu.menu_drawer_flujo_entrenador)
                        setupActionsNavViewEntrenador()
                    }
                }

            } ?: run {

            }

        }

    }

    private fun setupActionsNavViewEmpleado() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.nav_modificar_perfil_empleado -> {
                    true
                }
                R.id.nav_ver_codigo_qr_empleado -> {
                    mostrarCodigoQr()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupActionsNavViewEntrenador() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId) {
                R.id.nav_modificar_perfil_entrenador -> {
                    true
                }
                else -> false
            }
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
            binding.appbar,
            R.string.abierto,
            R.string.cerrado
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

    }

}