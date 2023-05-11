package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.pasos_formulario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.dao.sportiva.databinding.FragmentConfirmarAsistencia2Binding
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.InscripcionesRecyclerViewAdapter
import es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.ComenzarSesionEntrenadorViewModel
import es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.ComenzarSesionEntrenadorViewModelState

class ConfirmarAsistencia2Fragment : Fragment() {

    private lateinit var binding: FragmentConfirmarAsistencia2Binding

    private val viewModel: ComenzarSesionEntrenadorViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmarAsistencia2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setState(ComenzarSesionEntrenadorViewModelState.Neutral)
    }

    private fun setupView() {
        setupRecyclerInscripciones()
    }

    private fun setupRecyclerInscripciones() {

        val adapter = InscripcionesRecyclerViewAdapter()

        viewModel.inscripciones.observe(viewLifecycleOwner) { inscripciones ->

            if (inscripciones.isNullOrEmpty()) {
                binding.rvInscripciones.visibility = View.GONE
                adapter.submitList(emptyList())
                // TODO MOSTRAR MENSAJE VACIO
                // TODO HACER QUE CUANDO SE PASE DEL PASO 2 AL 1 DIRECTAMENTE LE TIRE AL MENU PRINCIPAL PREGUNTANDO BORRANDO EL VIERWMODEL
            } else {
                binding.rvInscripciones.visibility = View.VISIBLE
                adapter.submitList(inscripciones)
                // TODO OCULTAR MENSAJE VACIO
            }

        }

        binding.rvInscripciones.adapter = adapter

    }

}