package es.dao.sportiva.ui.fragments.flujo_entrenador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.MaterialDatePicker
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentCrearSesionBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.ui.adapters.EntrenadoresParticipantesRecyclerViewAdapter
import es.dao.sportiva.ui.fragments.MainViewModel

class CrearSesionFragment : Fragment() {

    private lateinit var binding: FragmentCrearSesionBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val entrenadorViewModel: EntrenadorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCrearSesionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        setupObservers()
        setupListeners()
    }

    private fun setupListeners() = with(binding) {
        btnSeleccionarFecha.setOnClickListener { seleccionarFechaYHoraSesion() }
    }

    private fun seleccionarFechaYHoraSesion() {
        MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build()
            .show(parentFragmentManager, "DATE_PICKER_TAG")
    }

    private fun setupObservers() {
        setupRecyclerEntrenadores()
    }

    private fun setupRecyclerEntrenadores() {
        val adapter = EntrenadoresParticipantesRecyclerViewAdapter()
        entrenadorViewModel.entrenadoresParticipantes.observe(viewLifecycleOwner) { entrenadores ->
            entrenadores?.let {
                adapter.submitList(it)
            }
        }
        binding.rvEntrenadoresParticipantes.adapter = adapter

        // añado a eso a mi mismo
        val entrenador = mainViewModel.usuario.value
        entrenador?.let {
            if (it is Entrenador) {
                it.isCurrentSesion = true
                entrenadorViewModel.addEntrenadorParticipante(it)
            }
        }

    }

}