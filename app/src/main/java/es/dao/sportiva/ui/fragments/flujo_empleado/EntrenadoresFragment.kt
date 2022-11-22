package es.dao.sportiva.ui.fragments.flujo_empleado

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import es.dao.sportiva.databinding.FragmentEntrenadoresBinding
import es.dao.sportiva.ui.adapters.EntrenadoresRecyclerViewAdapter

class EntrenadoresFragment : Fragment() {

    private lateinit var binding: FragmentEntrenadoresBinding
    private val viewModel: EmpleadoViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntrenadoresBinding.inflate(inflater, container, false)
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
        setupRecyclerEntrenadores()
    }

    private fun setupRecyclerEntrenadores() {
        val adapter = EntrenadoresRecyclerViewAdapter()
        viewModel.entrenadores.observe(viewLifecycleOwner) { entrenadoresWrapper ->
            entrenadoresWrapper?.let {
                adapter.submitList(it)
            }
        }
        binding.recyclerEntenadores.adapter = adapter
    }

}