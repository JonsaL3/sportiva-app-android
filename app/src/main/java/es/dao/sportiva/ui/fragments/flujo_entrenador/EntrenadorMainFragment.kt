package es.dao.sportiva.ui.fragments.flujo_entrenador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentEntrenadorMainBinding
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.utils.DxImplementation

class EntrenadorMainFragment : Fragment() {

    private lateinit var binding: FragmentEntrenadorMainBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntrenadorMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupListeners()
    }

    private fun setupListeners() {
        binding.crearSesion.setOnClickListener { loadFragmentCrearSesion() }
        binding.comenzarSesion.setOnClickListener { loadFragmentComenzarSesion() }
    }

    private fun loadFragmentCrearSesion() {
        val action = EntrenadorMainFragmentDirections.actionEntrenadorMainFragmentToCrearSesionFragment3()
        findNavController().navigate(action)
    }

    private fun loadFragmentComenzarSesion() {
        val action = EntrenadorMainFragmentDirections.actionEntrenadorMainFragmentToComenzarSesionFragment3()
        findNavController().navigate(action)
    }

}