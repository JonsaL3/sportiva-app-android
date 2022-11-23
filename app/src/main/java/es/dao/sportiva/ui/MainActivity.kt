package es.dao.sportiva.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ActivityMainBinding
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.ui.fragments.MainViewModel
import es.dao.sportiva.ui.fragments.flujo_empleado.EmpleadoViewModel

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

    private fun observeUsuario() {

        viewModel.usuario.observe(this) { usuario ->

            usuario?.let {

                when (it) {
                    is Empleado -> {
                        binding.navView.menu.clear()
                        binding.navView.inflateMenu(R.menu.menu_drawer_flujo_empleado)
                    }
                    else -> {
                        binding.navView.menu.clear()
                        binding.navView.inflateMenu(R.menu.menu_drawer_flujo_entrenador)
                    }
                }

            } ?: run {

            }

        }

    }

    private fun setupDrawer() {
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