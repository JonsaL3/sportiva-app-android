package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentFechaYHora3Binding
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorState
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorViewModel
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@AndroidEntryPoint
class FechaYHora3Fragment : Fragment() {

    private lateinit var binding: FragmentFechaYHora3Binding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CrearSesionEntrenadorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFechaYHora3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setState(CrearSesionEntrenadorState.Neutral)
    }

    private fun setupListeners() = with(binding) {
        btnEditarFecha.setOnClickListener { solicitarFecha() }
        btnEditarHora.setOnClickListener { solicitarHora() }
    }

    private fun solicitarFecha() {

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Fecha de la sesión")
            .build()

        datePicker.show(parentFragmentManager, "datePicker")
        datePicker.addOnPositiveButtonClickListener { date ->
            val localDate = Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDate()
            viewModel.setFecha(localDate)
        }

    }

    private fun solicitarHora() {

        val timePicker = MaterialTimePicker.Builder()
            .setTitleText("Hora de la sesión")
            .build()

        timePicker.show(parentFragmentManager, "timePicker")
        timePicker.addOnPositiveButtonClickListener { viewTime ->
            val hora = timePicker.hour
            val minuto = timePicker.minute
            val localTime = LocalTime.of(hora, minuto)
            viewModel.setHora(localTime)
        }

    }

}