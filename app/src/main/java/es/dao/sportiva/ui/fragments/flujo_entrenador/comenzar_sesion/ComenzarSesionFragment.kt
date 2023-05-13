package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentComenzarSesionBinding
import es.dao.sportiva.enum.PasoFormularioComenzarSesion
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.ComenzarSesionViewPagerAdapter
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ComenzarSesionFragment : Fragment() {

    private lateinit var binding: FragmentComenzarSesionBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: ComenzarSesionEntrenadorViewModel by activityViewModels()

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

                is ComenzarSesionEntrenadorViewModelState.SesionCreadaCorrectamente -> {

                    val action = {
                        viewModel.resetViewModel()
                        Navigation.findNavController(requireView()).navigateUp()
                        Unit
                    }

                    DxImplementation.mostrarDxLottie(
                        context = requireContext(),
                        titulo = "Éxito",
                        mensaje = "Se ha registrado el comienzo de la sesión correctamente.",
                        lottie = R.raw.sesion_creada_correctamente,
                        onAccept = action
                    )

                }

                /**
                 * En caso de que haya gente ausente a la sesión.
                 */
                is ComenzarSesionEntrenadorViewModelState.FaltaGentePorConfirmar -> {

                    val action = {
                        viewModel.comenzarSesion(true)
                    }

                    DxImplementation.mostrarDxConfirmacion(
                        context = requireContext(),
                        mensaje = "Los usuarios " + state.ausentes.joinToString(", ") { it.nombre } + " no han confirmado su asistencia. ¿Deseas continuar de todas formas?",
                        titulo = "Atención",
                        onAccept = action
                    )

                }

            }

        }

    }

    /**
     * Elementos agenos a la máquina de estados del fragmento.
     */
    private fun setupFragment() {
        setupPopBackStack()
        setupViewPager()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() {

        uiState.observableState.observe(viewLifecycleOwner) { state ->

            if (state == UiState.State.LOADING) {
                binding.btnFinalizar.isEnabled = false
                binding.btnScanner.isEnabled = false
            } else {
                binding.btnFinalizar.isEnabled = true
                binding.btnScanner.isEnabled = true
            }

        }

    }

    private fun setupPopBackStack() {

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            if (viewPager.currentItem > 0) {
                val onAccept = {
                    viewModel.resetViewModel()
                    Navigation.findNavController(requireView()).popBackStack()
                    Unit
                }

                DxImplementation.mostrarDxConfirmacion(
                    context = requireContext(),
                    titulo = "Descartar cambios",
                    mensaje = "Estás tratando de volver atrás y se descartarán los cambios realizados. ¿Estás seguro?",
                    onAccept = onAccept
                )
            } else {
                viewModel.resetViewModel()
                Navigation.findNavController(requireView()).popBackStack()
            }

        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun setupViewPager() {
        val dotsIndicator = binding.dotsIndicator
        viewPager = binding.vpCrearSesion
        val adapter = ComenzarSesionViewPagerAdapter(
            fragmentManager = parentFragmentManager,
            lifecycle = lifecycle
        )

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {

                when (PasoFormularioComenzarSesion.fromInt(position)) {

                    PasoFormularioComenzarSesion.SELECCIONAR_SESION -> {
                        binding.btnFinalizar.visibility = View.GONE
                        binding.btnScanner.visibility = View.GONE
                    }

                    PasoFormularioComenzarSesion.CONFIRMAR_ASISTENCIA -> {
                        binding.btnFinalizar.visibility = View.VISIBLE
                        binding.btnScanner.visibility = View.VISIBLE
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

            DxImplementation.mostrarDxConfirmacion(
                context = requireContext(),
                titulo = "Comenzar sesión",
                mensaje = "¿Estás seguro de que deseas marcar la sesión como comenzada y prodecer a impartirla?",
                onAccept = {
                    this@ComenzarSesionFragment.viewModel.comenzarSesion(false)
                }
            )

        }

        btnScanner.setOnClickListener {
            escanearQr()
        }

    }

    private fun escanearQr() {

        if (!hasCameraPermission()) {
            solicitarPermisos()
            return
        }

        val onQrScanned = { qr: String ->
            viewModel.marcarInscripcionDesdeQrIfCoincide(qr)
        }

        DxImplementation.mostrarDxLectorQr(
            context = requireContext(),
            onQrScanned = onQrScanned
        )

    }

    /**
     * Permisos para la cámara
     */
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                escanearQr()
            } else {
                Toast.makeText(requireContext(), "Permiso de cámara para barcode denegado.", Toast.LENGTH_SHORT).show()
            }
        }

    private fun solicitarPermisos() {
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

}