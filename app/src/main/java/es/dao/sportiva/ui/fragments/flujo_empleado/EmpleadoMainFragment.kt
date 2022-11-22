package es.dao.sportiva.ui.fragments.flujo_empleado

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentMainBinding
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.adapters.EmpleadoPrincipalViewPagerAdapter
import es.dao.sportiva.ui.fragments.MainViewModel

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        cargarDatos()
        setupAppbarFlujoEmpleado()
        setupBottomBar()
        setupViewPager()
        setupDrawer()
    }

    private fun cargarDatos() {

        viewModel.findEntrenadoresByIdEmpresa(
            idEmpresa = (mainViewModel.usuario.value as Empleado).empresa.id
        )

        viewModel.findSesionesDisponibles(
            (mainViewModel.usuario.value as Empleado).empresa.id
        )

    }

    private fun setupDrawer() {
        val drawer = binding.drawerLayout
        val toggle = ActionBarDrawerToggle(
            activity,
            drawer,
            binding.appbarFlujoEmpleado,
            R.string.abierto,
            R.string.cerrado
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupBottomBar() = with(binding) {

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.sesiones_menu -> {
                    viewpagerEmpleado.currentItem = 0
                    true
                }
                R.id.trainers_menu -> {
                    viewpagerEmpleado.currentItem = 1
                    true
                }
                else -> false
            }
        }

    }

    private fun setupViewPager() = with(binding) {
        viewpagerEmpleado.adapter = EmpleadoPrincipalViewPagerAdapter(requireActivity() as MainActivity)
        //  TODO que cambie el icono de la bottombar al cambiar de pagina
    }

}