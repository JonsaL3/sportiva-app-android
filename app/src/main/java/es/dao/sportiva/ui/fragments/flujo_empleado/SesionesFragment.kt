package es.dao.sportiva.ui.fragments.flujo_empleado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import es.dao.sportiva.databinding.FragmentSesionesBinding
import es.dao.sportiva.ui.adapters.SesionesRecyclerViewAdapter

class SesionesFragment : Fragment() {

    private lateinit var binding: FragmentSesionesBinding
    private val viewModel: EmpleadoViewModel by activityViewModels()

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
    }

    private fun setupView() {
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