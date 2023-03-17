package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.databinding.FragmentCrearSesionBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.ui.adapters.EntrenadoresParticipantesRecyclerViewAdapter
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.utils.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class CrearSesionFragment : Fragment() {

    private lateinit var binding: FragmentCrearSesionBinding

    private val viewModel: CrearSesionEntrenadorViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var uiState: UiState

    private val seleccionarArchivoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val image = result.data?.data
            val bitmap = image!!.getBitmap(requireContext())

            with(binding) {
                // Muestro la imagen que acabo de seleccionar
                sivImagenSeleccionada.visibility = View.VISIBLE
                sivSeleccioneImagen.visibility = View.GONE
                sivImagenSeleccionada.setImageBitmap(bitmap)
                tvFoto.visibility = View.GONE

                // Establezco los valores por defecto del otro campo, ya que solo podré realizar / seleccionaruna imagen
                sivFotoRealizada.visibility = View.GONE
                sivCamara.visibility = View.VISIBLE
                tvHacerFoto.visibility = View.VISIBLE
            }

        }
    }

    private val realizarFotografiaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val image = result.data?.data
            val bitmap = image!!.getBitmap(requireContext())

            with(binding) {
                // Muestro la imagen que acabo de realizar
                sivFotoRealizada.visibility = View.VISIBLE
                sivCamara.visibility = View.GONE
                sivFotoRealizada.setImageBitmap(bitmap)
                tvHacerFoto.visibility = View.GONE

                // Establezco los valores por defecto del otro campo, ya que solo podré realizar / seleccionaruna imagen
                sivImagenSeleccionada.visibility = View.GONE
                sivSeleccioneImagen.visibility = View.VISIBLE
                tvFoto.visibility = View.VISIBLE
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrearSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.usuario.value?.let {
            it as Entrenador
            viewModel.cargarDatosIniciales(it.empresaAsignada.id)
        }
        setupView()
    }

    private fun setupView() {
        setupObservers()
        setupListeners()
        setupDatosFormulario()
    }

    private fun setupDatosFormulario() {
        binding.etNombreSesion.doAfterTextChanged { viewModel.setTituloSession(it.toString()) }
        binding.etDescripcionSesion.doAfterTextChanged { viewModel.setDescripcionSession(it.toString()) }
        binding.etSubtituloSesion.setOnClickListener { viewModel.setSubtituloSession(it.toString()) }
    }

    private fun setupListeners() = with(binding) {
        btnSeleccionarFecha.setOnClickListener { seleccionarFechaYHoraSesion() }
        cvSeleccioneImagen.setOnClickListener { seleccionarArchivo() }
        cvRealizarImagen.setOnClickListener { realizarImagen() }
        btnAnadirEntrenador.setOnClickListener { agregarEntrenadores() }
    }

    private fun realizarImagen() {
        val intent = Intent(requireContext(), CamaraActivity::class.java)
        realizarFotografiaResult.launch(intent)
    }

    private fun seleccionarFechaYHoraSesion() {
        // Fecha
        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Fecha de la sesión")
                .build()
        datePicker.show(parentFragmentManager, "datePicker")
        datePicker.addOnPositiveButtonClickListener { date ->
            // Una vez tengo la fecha, selecciono la hora
            val timePicker = MaterialTimePicker.Builder()
                    .setTitleText("Hora de la sesión")
                    .build()
            timePicker.show(parentFragmentManager, "timePicker")
            timePicker.addOnPositiveButtonClickListener { viewTime -> // time is a view
                // calculo el total del date mas el time
                val mDate = date.toLocalDateTime()
                val mTime = timePicker.hour to timePicker.minute
                val mDateTime = mDate.withHour(mTime.first).withMinute(mTime.second)
                viewModel.setFechaSession(mDateTime)
            }

        }
    }

    private fun setupObservers() {
        setupRecyclerEntrenadores()
        setupFechaSeleccionadaObserver()
    }

    private fun setupFechaSeleccionadaObserver() {
        viewModel.fechaSesion.observe(viewLifecycleOwner) { fecha ->

            if (fecha.isAfter(LocalDateTime.now())) {
                if (fecha != Constantes.DEFAULT_DATE) {
                    binding.tvFechaSesion.text = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    binding.tvFechaSesion.setTypeface(null, Typeface.BOLD)
                }
            } else {
                uiState.setError("No puedes seleccionar una fecha anterior a la actual. Aún no se puede viajar al pasado...")
            }

        }
    }

    private fun setupRecyclerEntrenadores() {

        val adapter = EntrenadoresParticipantesRecyclerViewAdapter()
        viewModel.entrenadoresParticipantes.observe(viewLifecycleOwner) { entrenadores ->
            entrenadores?.let { entrenadorWrapper ->
                adapter.submitList(entrenadorWrapper.filter { it.isSeleccionadoParaSerParticipante }.map { it.copy() })
            }
        }
        binding.rvEntrenadoresParticipantes.adapter = adapter

        // añado a eso a mi mismo
        val entrenador = mainViewModel.usuario.value
        entrenador?.let {
            if (it is Entrenador) {
                it.isCurrentSesion = true
                it.isSeleccionadoParaSerParticipante = true
                viewModel.entrenadoresParticipantes.value = EntrenadorWrapper()
                viewModel.entrenadoresParticipantes.value?.add(it)
            }
        }

    }

    private fun agregarEntrenadores() {

        val onEntrenadoresSelected = { entrenadores: EntrenadorWrapper ->
            viewModel.addEntrenadoresParticipantes(entrenadores)
        }

        DxImplementation.mostrarDxSeleccionarEntrenador(
            context = requireContext(),
            entrenadores = viewModel.entrenadoresDisponibles.value,
            idCreador = mainViewModel.usuario.value?.id ?: -1,
            onEntrenadoresSelected = onEntrenadoresSelected
        )

    }

    private fun seleccionarArchivo() {

        seleccionarArchivoResult.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        })

    }

}