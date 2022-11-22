package es.dao.sportiva.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import es.dao.sportiva.databinding.ActivityMainBinding
import es.dao.sportiva.ui.fragments.flujo_empleado.EmpleadoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: EmpleadoViewModel by viewModels() // TODO AHORA ES ESPECIFICO DE EMPLEADOS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)
    }

}