package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentComenzarSesionBinding
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.InscripcionesRecyclerViewAdapter
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import javax.inject.Inject

@AndroidEntryPoint
class ComenzarSesionFragment : Fragment() {

    private lateinit var binding: FragmentComenzarSesionBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: ComenzarSesionEntrenadorViewModel by viewModels()

    @Inject lateinit var uiState: UiState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComenzarSesionBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.entrenadorViewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupDatosIniciales()
        setupListeners()
        setupObservers()
        setupOnBackPressed()
    }

    private fun seleccionarImagen() {
        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                // Handle the Intent
            }
        }
    }

    private fun setupDatosIniciales() {
        mainViewModel.usuario.value?.let {
            viewModel.cargarDatosGenerales(it.id)
        } ?: kotlin.run {
            uiState.setError(getString(R.string.error_current_usuario))
        }
    }

    private fun setupObservers() {
        setupRecyclerInscripciones()
    }

    private fun setupRecyclerInscripciones() {
        val adapter = InscripcionesRecyclerViewAdapter()
        viewModel.inscripcionesSesion.observe(viewLifecycleOwner) { inscripciones ->
            inscripciones?.let {
                adapter.submitList(it)
            }
        }
        binding.rvInscripciones.adapter = adapter
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {

            DxImplementation.mostrarDxConfirmacion(
                context = requireContext(),
                titulo = getString(R.string.deshacer_cambios),
                mensaje = getString(R.string.seguro_volver_atras)
            ) {
                findNavController().popBackStack()
            }

        }
    }

    private fun setupListeners() = with(binding) {
        btnSeleccionarSesion.setOnClickListener { seleccionarSesion() }
        btnEscanearQr.setOnClickListener { mostrarBarcodeScanner() }
    }

    private fun mostrarBarcodeScanner() {

        val onQrScanned = { qr: String ->

        }

        DxImplementation.mostrarDxLectorQr(
            context = requireContext(),
            onQrScanned = onQrScanned
        )
    }

    private fun seleccionarSesion() {

        DxImplementation.mostrarDxListaSesionesSeleccionar(
            requireContext(),
            viewModel.sesionesCreadasPorElEntrenador.value
        ) { sesion ->
            viewModel.setSesion(sesion)
        }

    }

}