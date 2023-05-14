package es.dao.sportiva.ui.fragments.flujo_empleado

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentMainBinding
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.adapters.EmpleadoPrincipalViewPagerAdapter
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.utils.DxImplementation
import kotlinx.coroutines.launch

class EmpleadoMainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: EmpleadoViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.empleado = mainViewModel.usuario.value as Empleado
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStateMachine()
        setupFragment()
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.obtenerDatos(
            idEmpresa = (mainViewModel.usuario.value as Empleado).empresa.id,
            idEmpleado = (mainViewModel.usuario.value as Empleado).id
        )
    }

    /**
     * Máquina de estados del fragmento.
     */
    private fun setupStateMachine() = viewLifecycleOwner.lifecycleScope.launch() {

        viewModel.state.flowWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.CREATED
        ).collect { state ->

            when (state) {

                is EmpleadoViewModelState.Neutral -> { }

                is EmpleadoViewModelState.ApuntadoCorrectamente -> {

                    DxImplementation.mostrarDxLottieCentro(
                        context = requireContext(),
                        mensaje = "¡Inscrito correctamente a la sesión: '${state.sesion.titulo.trim()}'!",
                        titulo = "Se ha inscrito correctamente a la sesión.",
                        lottie = R.raw.sesion_creada_correctamente,
                        onAccept = { }
                    )

                }

                is EmpleadoViewModelState.DesapuntadoCorrectamente -> {

                    DxImplementation.mostrarDxLottieCentro(
                        context = requireContext(),
                        mensaje = "Usted acaba de retirar su inscripción a la sesión: '${state.sesion.titulo.trim()}'",
                        titulo = "Se ha desinscrito correctamente.",
                        lottie = R.raw.uninscritto_lottie_anim,
                        onAccept = { }
                    )

                }

            }

        }

    }

    /**
     * Elementos agenos a la máquina de estados del fragmento.
     */
    private fun setupFragment() {
        setupViewPager()
    }

    private fun setupViewPager() = with(binding) {
        viewpagerEmpleado.adapter = EmpleadoPrincipalViewPagerAdapter(requireActivity() as MainActivity)
    }

}