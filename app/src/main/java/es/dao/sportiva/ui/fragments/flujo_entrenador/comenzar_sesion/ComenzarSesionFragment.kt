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

            // TODO AL COMENZAR SESIÓN POPBACK DESCARTANDO SIEMPRE,  LIMPIAR EL VIEWMODEL SIEMPRE EN SELECCIONAR ACCIÓM
            // TODO COMPRIMIR IMAGENES
            // TODO DRAWER
            // TODO PANTALLA NUEVA VERSIÓN BONITA
            // TODO AFINAR FILTRO DE SESIONES DEL DÍA
            // TODO BLOQUEAR BOTONES MIENTRAS SE ATACA AL SERVIDOR

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
                        mensaje = "La sesión ha dado comienzo correctamente.",
                        lottie = R.raw.sesion_creada_correctamente,
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
        setupPopBackStack() // TODO POP BACK A TODO DIRECTAMENTE SIN VOLVER A PASAR POR EL PASO 1, QUE EMPIECEN DE 0 SI VAN ATRAS
        setupViewPager()
        setupListeners()
    }

    private fun setupPopBackStack() {

        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            if (viewPager.currentItem == 0) {

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
                viewPager.currentItem = viewPager.currentItem - 1
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
            // TODO DX CONFIRMACIÓN
            this@ComenzarSesionFragment.viewModel.comenzarSesion()
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

        // TODO MUY IMPORTANTE, CREO QUE LA CÁMARA SE ME QUEDA
        // TODO EN SEGUNDO PLANO PORQUE CONSTANTE MENTE
        // TODO SALTA EN LOS WARNINGS UNA EXCEPCION
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