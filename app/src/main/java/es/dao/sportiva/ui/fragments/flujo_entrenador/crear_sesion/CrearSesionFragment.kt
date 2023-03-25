package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.databinding.FragmentCrearSesionBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.CrearSesionViewPagerAdapter
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class CrearSesionFragment : Fragment() {

    private lateinit var binding: FragmentCrearSesionBinding

    private val viewModel: CrearSesionEntrenadorViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var uiState: UiState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrearSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setupView()
        setupStates()
    }

    /**
     * Máquina de estados del fragmento
     */
    private fun setupStates() = viewLifecycleOwner.lifecycleScope.launch() {

        viewModel.state.flowWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.CREATED
        ).collect { state ->

            when (state) {

                is CrearSesionEntrenadorState.Neutral -> {

                }

                is CrearSesionEntrenadorState.SeleccionandoEntrenador -> {

                    // TODO NO FUNCIONA LA MAQUINA DE ESTADOS

                    val onEntrenadoresSelected = { list: EntrenadorWrapper ->
                        viewModel.addEntrenadores(list)
                    }

                    DxImplementation.mostrarDxSeleccionarEntrenador(
                        context = requireContext(),
                        entrenadores = state.entrenadores,
                        idCreador = mainViewModel.usuario.value!!.id,
                        onEntrenadoresSelected = onEntrenadoresSelected
                    )

                }

            }

        }

    }

    /**
     * Inicialización del fragmento
     */
    private fun setupView() {
        setupViewPager()
        setupListeners()
    }

    private fun setupListeners() {

        binding.btnAnadirEntrenador.setOnClickListener {
            viewModel.findEntrenadoresByIdEmpresa((mainViewModel.usuario.value!! as Entrenador).empresaAsignada.id)
        }

    }

    private fun setupViewPager() {

        val dotsIndicator = binding.dotsIndicator
        val viewPager = binding.vpCrearSesion
        val adapter = CrearSesionViewPagerAdapter(
            fragmentManager = parentFragmentManager,
            lifecycle = lifecycle
        )
        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)

        viewPager.isUserInputEnabled = false

    }

}