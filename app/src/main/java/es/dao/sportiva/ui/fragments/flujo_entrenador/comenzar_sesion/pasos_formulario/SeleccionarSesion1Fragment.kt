package es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.pasos_formulario

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentSeleccionarSesion1Binding
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.SeleccionarSesionRecyclerViewAdapter
import es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.ComenzarSesionEntrenadorViewModel
import es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.ComenzarSesionEntrenadorViewModelState


class SeleccionarSesion1Fragment : Fragment() {

    private lateinit var binding: FragmentSeleccionarSesion1Binding

    private val viewModel: ComenzarSesionEntrenadorViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeleccionarSesion1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.obtenerSesionesDisponibles(mainViewModel.usuario.value!!.id)
        setupRecyclerSesionesDisponibles()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setState(ComenzarSesionEntrenadorViewModelState.Neutral)
    }

    private fun setupRecyclerSesionesDisponibles() {

        val onSessionSelected = { sesion: Sesion ->
            viewModel.setSesionSeleccionada(sesion)
        }

        val adapter = SeleccionarSesionRecyclerViewAdapter(onSessionSelected)

        viewModel.listaDeSesionesDisponibles.observe(viewLifecycleOwner) { sesionWrapper ->

            adapter.submitList(sesionWrapper)

            if (sesionWrapper.isNotEmpty()) {
                binding.rvSesiones.visibility = View.VISIBLE
                // TODO OCULTAR EL TEXTO DE QUE NO HAY SESIONES DISPONIBLES
            } else {
                binding.rvSesiones.visibility = View.GONE
                // TODO MOSTRAR EL TEXTO DE QUE NO HAY SESIONES DISPONIBLES
            }

        }

        binding.rvSesiones.adapter = adapter

    }

}