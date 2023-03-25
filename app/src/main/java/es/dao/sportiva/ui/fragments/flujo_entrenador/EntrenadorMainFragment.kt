package es.dao.sportiva.ui.fragments.flujo_entrenador

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentEntrenadorMainBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.utils.DxImplementation

@AndroidEntryPoint
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
        binding.entrenador = mainViewModel.usuario.value!! as Entrenador
        binding.lifecycleOwner = viewLifecycleOwner
        setupView()
    }

    /**
     * Inicialización del fragmento
     */
    private fun setupView() {
        setupListeners()
    }

    private fun setupListeners() {

        binding.llCrearSesion.setOnClickListener {
            loadFragmentCrearSesion()
        }

        binding.llComenzarSesion.setOnClickListener {
            loadFragmentComenzarSesion()
        }

    }

    /**
     * Navegacion hacia las pantallas de Crear Sesion y Comenzar Sesion
     */
    private fun loadFragmentCrearSesion() {
        val action = EntrenadorMainFragmentDirections.actionEntrenadorMainFragmentToCrearSesionFragment3()
        findNavController().navigate(action)
    }

    private fun loadFragmentComenzarSesion() {
        val action = EntrenadorMainFragmentDirections.actionEntrenadorMainFragmentToComenzarSesionFragment3()
        findNavController().navigate(action)
    }

}