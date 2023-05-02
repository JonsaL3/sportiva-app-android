package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentAforo4FragmentBinding
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorState
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorViewModel
import es.dao.sportiva.utils.Constantes

@AndroidEntryPoint
class Aforo4Fragment : Fragment() {

    private lateinit var binding: FragmentAforo4FragmentBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CrearSesionEntrenadorViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAforo4FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.constantes = Constantes
        binding.lifecycleOwner = viewLifecycleOwner
        setupColorsQueNoSeMeSeteanEnLaDefinicionDelTemaPorAlgunMotivo()
        setupCheckedChangedListener()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setState(CrearSesionEntrenadorState.Neutral)
    }

    private fun setupCheckedChangedListener() {
        binding.mcbAforoIlimitado.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tilAforo.requestFocus()
            } else {
                binding.tilAforo.clearFocus()
            }
        }
    }

    private fun setupColorsQueNoSeMeSeteanEnLaDefinicionDelTemaPorAlgunMotivo() {

        val states = arrayOf(
            intArrayOf(android.R.attr.state_focused),
            intArrayOf(android.R.attr.state_pressed),
        )

        val colors = intArrayOf(
            ResourcesCompat.getColor(resources, R.color.colorPrimary, null),
            ResourcesCompat.getColor(resources, R.color.colorPrimary, null),
        )

        val colorStateList = ColorStateList(states, colors)
        binding.tilAforo.setBoxStrokeColorStateList(colorStateList)

    }

}