package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentCrearSesionBinding
import es.dao.sportiva.enum.PasoFormularioCrearSesion
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.CrearSesionViewPagerAdapter
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class CrearSesionFragment : Fragment() {

    private lateinit var binding: FragmentCrearSesionBinding

    private val viewModel: CrearSesionEntrenadorViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var viewPager: ViewPager2

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
        setupStates()
        setupView()
    }

    /**
     * Máquina de estados del fragmento
     */
    private fun setupStates() = viewLifecycleOwner.lifecycleScope.launch() {

        viewModel.state.flowWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.CREATED
        ).collect { state ->

            Log.d("CrearSesionFragment;;;", "State: ${state.javaClass.simpleName}")

            when (state) {

                is CrearSesionEntrenadorState.Neutral -> { }

                /**
                 * Navegación de los distintos pasos del formulario
                 */
                is CrearSesionEntrenadorState.SolicitarInformacion -> {

                    if (state.paso == PasoFormularioCrearSesion.SOLICITAR_FECHA_Y_HORA) {

                        if (viewModel.camposDelPaso2Validos())
                            viewPager.currentItem = state.paso.ordinal
                        else
                            uiState.setError("Debes rellenar todos los campos.")

                    } else if (state.paso == PasoFormularioCrearSesion.SOLICITAR_AFORO) {

                        when (viewModel.camposDelPaso3Validos()) {
                            -1 -> uiState.setError("Debes rellenar todos los campos.")
                            -2 -> uiState.setError("La fecha y la hora de inicio de la sesión debe ser posterior a la fecha actual.")
                            else -> viewPager.currentItem = state.paso.ordinal
                        }

                    } else if (state.paso == PasoFormularioCrearSesion.SOLICITAR_IMAGENES) {

                        if (viewModel.comprobarCamposDelPaso4())
                            viewPager.currentItem = state.paso.ordinal
                        else
                            uiState.setError("Debes marcar la casilla de aforo ilimitado o establecer un aforo máximo, el cual debe ser superior a 0 (El aforo solo tiene en cuenta a los participantes no a los entrenadores).")

                    } else {
                        viewPager.currentItem = state.paso.ordinal
                    }

                }

                /**
                 * Cuando se descargan correctamente los entrenadores
                 */
                is CrearSesionEntrenadorState.SeleccionandoEntrenador -> {

                    val onEntrenadoresSelected = { list: EntrenadorWrapper ->
                        viewModel.addEntrenadores(list)
                    }

                    DxImplementation.mostrarDxSeleccionarEntrenador(
                        context = requireContext(),
                        onEntrenadoresSelected = onEntrenadoresSelected,
                        entrenadoresEnMiMismaEmpresa = state.entrenadores,
                        entrenadoresYaEnLaLista = viewModel.entrenadoresAnadidos.value ?: EntrenadorWrapper()
                    )

                }

                /**
                 * Tras un duro trabajo, finalmente enviamos la sesión al servidor pero pidiendo
                 * confirmación al usuario.
                 */
                is CrearSesionEntrenadorState.CrearSesion -> {

                    if (viewModel.camposDelPaso5Validos()) {

                        val onAccept = {
                            val entrenador = mainViewModel.usuario.value as Entrenador
                            viewModel.crearSesion(
                                context = requireContext(),
                                empresa = entrenador.empresaAsignada,
                                creador = entrenador,
                            )
                            Unit
                        }

                        DxImplementation.mostrarDxConfirmarCrearSesion(
                            context = requireContext(),
                            entrenadoresParticipantes = viewModel.entrenadoresAnadidos.value ?: EntrenadorWrapper(),
                            titulo = viewModel.tituloSesion.value ?: "",
                            fechaYHora = LocalDateTime.of(
                                viewModel.fecha.value ?: LocalDate.now(),
                                viewModel.hora.value ?: LocalTime.now()
                            ),
                            aforo = viewModel.aforo.value ?: 0,
                            onAccept = onAccept
                        )

                    } else {
                        uiState.setError("Debes realizar la imagen o seleccionar una existente de tu dispositivo.")
                    }

                }

                /**
                 * Si la sesión se crea correctamente...
                 */
                is CrearSesionEntrenadorState.SesionCreadaCorrectamente -> {

                    val action = {
                        viewModel.resetViewModel()
                        Navigation.findNavController(requireView()).navigateUp()
                        Unit
                    }

                    DxImplementation.mostrarDxLottie(
                        context = requireContext(),
                        titulo = "Éxito",
                        mensaje = "La sesión se ha creado correctamente.",
                        lottie = R.raw.sesion_creada_correctamente,
                        onAccept = action
                    )

                }

            }

        }

    }

    /**
     * Inicialización del fragmento
     */
    private fun setupView() {
        setupPopBackStack()
        setupViewPager()
        setupListeners()
        setupObservers()
    }

    private fun setupObservers() { // Aqui me interesa añadir acciones al estado loading, solo en este fragmento

        uiState.observableState.observe(viewLifecycleOwner) { uiState ->

            if (uiState == UiState.State.LOADING) {
                binding.btnContinuar.isEnabled = false
                binding.btnAnadirEntrenador.isEnabled = false
                binding.btnFinalizar.isEnabled = false
            } else {
                binding.btnContinuar.isEnabled = true
                binding.btnAnadirEntrenador.isEnabled = true
                binding.btnFinalizar.isEnabled = true
            }

        }

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

    private fun setupListeners() = with(binding) {

        btnAnadirEntrenador.setOnClickListener {
            this@CrearSesionFragment.viewModel.findEntrenadoresByIdEmpresa((mainViewModel.usuario.value!! as Entrenador).empresaAsignada.id)
        }

        btnContinuar.setOnClickListener {
            this@CrearSesionFragment.viewModel.setState(
                CrearSesionEntrenadorState.SolicitarInformacion(
                    PasoFormularioCrearSesion.fromInt(viewPager.currentItem + 1) // trabajar con el enumerado me complica aqui las cosas pero en otros sitios me lo simplifica.
                )
            )
        }

        btnFinalizar.setOnClickListener {
            this@CrearSesionFragment.viewModel.setState(CrearSesionEntrenadorState.CrearSesion)
        }

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

                when (PasoFormularioCrearSesion.fromInt(position)) {

                    PasoFormularioCrearSesion.SOLICITAR_ENTRENADORES -> {
                        binding.btnAnadirEntrenador.visibility = View.VISIBLE
                    }

                    PasoFormularioCrearSesion.SOLICITAR_IMAGENES -> {
                        binding.btnContinuar.visibility = View.GONE
                        binding.btnFinalizar.visibility = View.VISIBLE
                    }

                    else -> {
                        binding.btnContinuar.visibility = View.VISIBLE
                        binding.btnAnadirEntrenador.visibility = View.GONE
                        binding.btnFinalizar.visibility = View.GONE
                    }
                }

            }

        })

        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)
        viewPager.isUserInputEnabled = false
    }

}