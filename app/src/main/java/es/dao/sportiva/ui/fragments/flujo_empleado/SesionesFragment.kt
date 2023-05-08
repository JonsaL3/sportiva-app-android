package es.dao.sportiva.ui.fragments.flujo_empleado

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.dao.sportiva.databinding.FragmentSesionesBinding
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.SesionesRecyclerViewAdapter


class SesionesFragment : Fragment() {

    private lateinit var binding: FragmentSesionesBinding
    private val viewModel: EmpleadoViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSesionesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.obtenerDatos((mainViewModel.usuario.value!! as Empleado).empresa.id)
    }

    private fun setupView() {

        val matrix = ColorMatrix()
        matrix.setSaturation(0f)

        val filter = ColorMatrixColorFilter(matrix)

        binding.backgroundImage.colorFilter = filter

        setupObservers()
    }

    private fun setupObservers() {
        setupRecyclerSesiones()
    }

    private fun setupRecyclerSesiones() {
        val adapter = SesionesRecyclerViewAdapter()
        viewModel.sesionesDisponibles.observe(viewLifecycleOwner) { sesionWrapper ->
            sesionWrapper?.let {
                adapter.submitList(it)
            }
        }
        binding.recyclerSesiones.adapter = adapter
    }

}