package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.databinding.FragmentComenzarSesionBinding
import es.dao.sportiva.enum.PasoFormularioComenzarSesion
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.CrearSesionViewPagerAdapter
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ComenzarSesionFragment : Fragment() {

    private lateinit var binding: FragmentComenzarSesionBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: ComenzarSesionEntrenadorViewModel by viewModels()

    private lateinit var viewPager: ViewPager2

    @Inject lateinit var uiState: UiState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComenzarSesionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupFragment()
        setupStateMachine()
    }

    /**
     * Máquina de estados del fragmento
     */
    private fun setupStateMachine() = viewLifecycleOwner.lifecycleScope.launch() {

        viewModel.state.flowWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.CREATED
        ).collect { state ->

            Log.d("ComenzarSesionFragment;;;", "State: ${state.javaClass.simpleName}")

            when(state) {

                is ComenzarSesionEntrenadorViewModelState.Neutral -> { }

                /**
                 * Navegación entre los distintos pasos del formulario
                 */
                is ComenzarSesionEntrenadorViewModelState.IrAPasoEspecifico -> {
                    viewPager.currentItem = state.paso.ordinal
                }

            }

        }

    }

    /**
     * Elementos agenos a la máquina de estados del fragmento.
     */
    private fun setupFragment() {
        setupViewPager()
        setupListeners()
    }

    private fun setupViewPager() {
        val dotsIndicator = binding.dotsIndicator
        viewPager = binding.vpCrearSesion
        val adapter = CrearSesionViewPagerAdapter(
            fragmentManager = parentFragmentManager,
            lifecycle = lifecycle
        )

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {

                when (PasoFormularioComenzarSesion.fromInt(position)) {

                    PasoFormularioComenzarSesion.SELECCIONAR_SESION -> {
                        binding.btnFinalizar.visibility = View.GONE
                    }

                    PasoFormularioComenzarSesion.CONFIRMAR_ASISTENCIA -> {
                        binding.btnFinalizar.visibility = View.VISIBLE
                    }

                }

            }

        })

        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)
        viewPager.isUserInputEnabled = false
    }

    private fun setupListeners() = with(binding) {

        btnFinalizar.setOnClickListener {
            // TODO DX CONFIRMACIÓN
            viewModel.comenzarSesion()
        }

    }

}