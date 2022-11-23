package es.dao.sportiva.ui.fragments.flujo_entrenador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentEntrenadorMainBinding

class EntrenadorMainFragment : Fragment() {

    private lateinit var binding: FragmentEntrenadorMainBinding
    private val viewModel: EntrenadorViewModel by activityViewModels()

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
        // todo gonzalo estas con la movida de que no puedes navegar a estos fragmentos
    }

    private fun loadFragmentComenzarSesion() {

    }

}