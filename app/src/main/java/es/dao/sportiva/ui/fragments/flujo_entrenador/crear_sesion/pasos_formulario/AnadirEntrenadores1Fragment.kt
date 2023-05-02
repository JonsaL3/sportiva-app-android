package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentAnadirEntrenadores1Binding
import es.dao.sportiva.databinding.ItemEntrenadorParticipanteBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.entrenador.EntrenadorWrapper
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.EntrenadoresParticipantesRecyclerViewAdapter
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorState
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorViewModel
import es.dao.sportiva.utils.DxImplementation

/**
 * Este fragment no tiene máquina de estados porque es dependiente de la maquina del fragmento que contiene
 * el viewpager.
 */
@AndroidEntryPoint
class AnadirEntrenadores1Fragment : Fragment() {

    private lateinit var binding: FragmentAnadirEntrenadores1Binding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CrearSesionEntrenadorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnadirEntrenadores1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerListaDeEntrenadores()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setState(CrearSesionEntrenadorState.Neutral)
    }

    private fun setupRecyclerListaDeEntrenadores() {

        // Cuando se elimine un entrenador de la lista se hará de la siguiente forma
        val onDelete = { entrenador: Entrenador, binding: ItemEntrenadorParticipanteBinding ->

            val onAccept = {
                viewModel.removeEntrenador(entrenador)
            }

            DxImplementation.mostrarDxConfirmacion(
                context = requireContext(),
                titulo = "Eliminar entrenador",
                mensaje = "¿Estás seguro de que quieres eliminar a ${entrenador.nombre} de la lista de entrenadores participantes?",
                onAccept = onAccept
            )

        }

        // Preparo el adapter con su observer al mutable live data
        val adapter = EntrenadoresParticipantesRecyclerViewAdapter(onDelete)
        viewModel.entrenadoresAnadidos.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        binding.rvEntrenadores.adapter = adapter

        // Por defecto apareceré yo como entrenador participante y no podré ser borrado
        if (!viewModel.entrenadoresAnadidos.value!!.any { it.isCurrentSesion}) {
            val wrapper = EntrenadorWrapper()
            val yo = mainViewModel.usuario.value!! as Entrenador
            yo.isCurrentSesion = true
            wrapper.add(yo)
            viewModel.addEntrenadores(wrapper)
        }

    }

}