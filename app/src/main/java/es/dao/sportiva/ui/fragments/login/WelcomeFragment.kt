package es.dao.sportiva.ui.fragments.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.dao.sportiva.databinding.FragmentWelcomeBinding
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.RegisterViewPagerAdapter
import es.dao.sportiva.utils.DxImplementation

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAnimations()
        setupView()
    }

    private fun setupView() = with(binding) {

        btnIniciarSesion.setOnClickListener {
            DxImplementation.mostrarDxLogin(requireContext()) { correo, contrasena ->

                viewModel.doLogin(correo, contrasena, requireContext()) { usuario ->

                    if (usuario is Empleado) {
                        val action =
                            WelcomeFragmentDirections.actionLoginFragmentToEmpleadoMainFragment()
                        findNavController().navigate(action)
                    } else if (usuario is Entrenador) {
                        val action =
                            WelcomeFragmentDirections.actionLoginFragmentToEntrenadorMainFragment3()
                        findNavController().navigate(action)
                    }

                }
            }
        }

        val dotsIndicator = dotsIndicator
        val viewPager = viewPager
        val adapter = RegisterViewPagerAdapter((requireActivity() as MainActivity))

        btnRegistrarse.setOnClickListener {
            startRegistrationProcess()
            (requireActivity() as MainActivity).actionOnBackPressed = { restoreWelcomeFragment() }
        }
        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)

    }

    private fun setupAnimations() = with(binding) {

        logoSportiva.animate().alpha(1f).setDuration(500).start()

        txtBienvenido.animate().translationX(0f).setDuration(500).start()
        txtBienvenido.animate().alpha(1f).setDuration(600).start()

        txtBienvenidoMsg.animate().translationX(0f).setDuration(600).start()
        txtBienvenidoMsg.animate().alpha(1f).setDuration(700).start()

        btnIniciarSesion.animate().translationX(0f).setDuration(700).start()
        btnRegistrarse.animate().translationX(0f).setDuration(800).start()

        btnIniciarSesion.animate().alpha(1f).setDuration(800).start()
        btnRegistrarse.animate().alpha(1f).setDuration(900).start()
    }

    private fun startRegistrationProcess() = with(binding) {

        informationRegisterConstraintLayout.animate().alpha(1f).setDuration(500).start()

        txtBienvenido.animate().translationX(-300f).setDuration(500).start()
        txtBienvenido.animate().alpha(0f).setDuration(600).start()

        txtBienvenidoMsg.animate().translationX(-300f).setDuration(600).start()
        txtBienvenidoMsg.animate().alpha(0f).setDuration(700).start()

        btnIniciarSesion.animate().translationX(-300f).setDuration(700).start()
        btnRegistrarse.animate().translationX(-300f).setDuration(800).start()

        btnIniciarSesion.animate().alpha(0f).setDuration(800).start()
        btnRegistrarse.animate().alpha(0f).setDuration(900).start()

    }

    private fun restoreWelcomeFragment() = with(binding) {

        txtBienvenido.animate().translationX(0f).setDuration(500).start()
        txtBienvenido.animate().alpha(1f).setDuration(600).start()

        txtBienvenidoMsg.animate().translationX(0f).setDuration(600).start()
        txtBienvenidoMsg.animate().alpha(1f).setDuration(700).start()

        btnIniciarSesion.animate().translationX(0f).setDuration(700).start()
        btnRegistrarse.animate().translationX(0f).setDuration(800).start()

        btnIniciarSesion.animate().alpha(1f).setDuration(800).start()
        btnRegistrarse.animate().alpha(1f).setDuration(900).start()

        informationRegisterConstraintLayout.animate().alpha(0f).setDuration(900).start()

        Handler(Looper.getMainLooper()).postDelayed({
            viewPager.currentItem = 0
        }, 900)

        (requireActivity() as MainActivity).actionOnBackPressed = null
    }

}