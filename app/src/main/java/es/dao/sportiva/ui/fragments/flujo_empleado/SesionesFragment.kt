package es.dao.sportiva.ui.fragments.flujo_empleado

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.dxcustomlibrary.gone
import com.example.dxcustomlibrary.visible
import es.dao.sportiva.databinding.FragmentSesionesBinding
import es.dao.sportiva.databinding.ItemSesionBinding
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.sesion.Sesion
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
        setupFragment()
    }

    private fun setupFragment() {
        setupObservers()
    }

    private fun setupObservers() {
        setupRecyclerSesiones()
    }

    private fun setupRecyclerSesiones() {

        val currentEmpleado = mainViewModel.usuario.value!! as Empleado

        val onApuntarse = { binding: ItemSesionBinding, sesion: Sesion ->

            binding.btnApuntarse.isClickable = false

            viewModel.inscribirEmpleadoASesion(
                empleado = currentEmpleado,
                sesion = sesion,
            )

        }

        val onDesapuntarse = { binding: ItemSesionBinding, sesion: Sesion ->

            binding.btnDesapuntarse.isClickable = false

            viewModel.desinscribirEmpleadoASesion(
                idEmpleado = currentEmpleado.id,
                idSesion = sesion.id
            )

        }

        val adapter = SesionesRecyclerViewAdapter(
            onApuntarse = onApuntarse,
            onDesapuntarse = onDesapuntarse
        )

        viewModel.sesionesDisponibles.observe(viewLifecycleOwner) { sesionWrapper ->
            sesionWrapper?.let {
                adapter.submitList(it)

                if (it.size > 0) {
                    binding.clNotSesiones.gone()
                    binding.recyclerSesiones.visible()
                } else {
                    binding.clNotSesiones.visible()
                    binding.recyclerSesiones.gone()
                }

            } ?: run {
                binding.clNotSesiones.visible()
                binding.recyclerSesiones.gone()
                adapter.submitList(listOf())
            }
        }

        binding.recyclerSesiones.adapter = adapter
    }

}