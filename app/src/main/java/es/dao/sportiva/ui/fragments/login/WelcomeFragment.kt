package es.dao.sportiva.ui.fragments.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dxcustomlibrary.visible
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentWelcomeBinding
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.RegisterViewPagerAdapter
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding
    private val viewModel: MainViewModel by activityViewModels()
    private val registroViewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var uiState: UiState

    private var countRegisterEntrenador: Int = 0

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
        setupInclude()
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
        binding.logoSportiva.setOnClickListener {
            countRegisterEntrenador++
            if(countRegisterEntrenador == 5){
                countRegisterEntrenador = 0
                registroEntrenadorLinearLayout.visible()
            }
        }
        viewPager.adapter = adapter
        dotsIndicator.attachTo(viewPager)

    }

    private fun setupAnimations() = with(binding) {

        viewPager.isUserInputEnabled = false

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

        viewPager.isUserInputEnabled = true

        informationRegisterConstraintLayout.animate().alpha(1f).setDuration(500).start()
        viewColor.animate().alpha(1f).setDuration(500).start()

        txtBienvenido.animate().translationX(-300f).setDuration(500).start()
        txtBienvenido.animate().alpha(0f).setDuration(600).start()

        txtBienvenidoMsg.animate().translationX(-300f).setDuration(600).start()
        txtBienvenidoMsg.animate().alpha(0f).setDuration(700).start()

        btnIniciarSesion.animate().translationX(-600f).setDuration(700).start()
        btnRegistrarse.animate().translationX(-600f).setDuration(800).start()

        btnIniciarSesion.animate().alpha(0f).setDuration(800).start()
        btnRegistrarse.animate().alpha(0f).setDuration(900).start()

    }

    private fun restoreWelcomeFragment() = with(binding) {

        viewPager.isUserInputEnabled = false

        txtBienvenido.animate().translationX(0f).setDuration(500).start()
        txtBienvenido.animate().alpha(1f).setDuration(600).start()

        txtBienvenidoMsg.animate().translationX(0f).setDuration(600).start()
        txtBienvenidoMsg.animate().alpha(1f).setDuration(700).start()

        btnIniciarSesion.animate().translationX(0f).setDuration(700).start()
        btnRegistrarse.animate().translationX(0f).setDuration(800).start()

        btnIniciarSesion.animate().alpha(1f).setDuration(800).start()
        btnRegistrarse.animate().alpha(1f).setDuration(900).start()

        informationRegisterConstraintLayout.animate().alpha(0f).setDuration(900).start()
        viewColor.animate().alpha(0f).setDuration(900).start()

        Handler(Looper.getMainLooper()).postDelayed({
            viewPager.currentItem = 0
        }, 900)

        (requireActivity() as MainActivity).actionOnBackPressed = null


    }

    private fun setupInclude() = with(binding.includeRegistroEntrenador){

        btnVolverRegistroEntrenador.setOnClickListener {
            this@WelcomeFragment.binding.registroEntrenadorLinearLayout.visibility = View.GONE
        }

        btnRegistrarse.setOnClickListener {
            if(checkDataIdEmpty()){
                Toast.makeText(requireContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{

                val onError = {
                    uiState.setError(requireContext().getString(R.string.error_al_registrarse))
                }

                val onSuccses = {
                    uiState.setSuccess()
                    //TODO
                    val action =
                        WelcomeFragmentDirections.actionLoginFragmentToEntrenadorMainFragment3()
                    findNavController().navigate(action)
                }

                val onFailure = {
                    uiState.setError(requireContext().getString(R.string.error_al_registrarse))
                }

                uiState.setLoadingFullScreen(
                    requireContext().getString(R.string.registrando_entrenador),
                    requireContext().getString(R.string.estamos_registrando_tu_cuenta_por_favor_espera_un_momento)
                )

                registroViewModel.registerEntrenador(onError, onSuccses, onFailure)
            }
        }
    }

    private fun checkDataIdEmpty(): Boolean = with(binding.includeRegistroEntrenador){
        return@with etNombre.text.toString().isEmpty() ||
                etApellido1.text.toString().isEmpty() ||
                etCorreo.text.toString().isEmpty() ||
                etContrasenia.text.toString().isEmpty() ||
                etApellido2.text.toString().isEmpty() ||
                etEstudios.text.toString().isEmpty() ||
                etSueldo.text.toString().isEmpty() ||
                etEmpresa.text.toString().isEmpty()
    }


}