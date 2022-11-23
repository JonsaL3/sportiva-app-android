package es.dao.sportiva.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import es.dao.sportiva.databinding.FragmentLoginBinding
import es.dao.sportiva.models.Empleado
import es.dao.sportiva.models.entrenador.Entrenador

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.doLogout()
    }

    private fun setupView() {
        setupDebugMode()
        setupListeners()
    }

    private fun setupDebugMode() {
        binding.etCorreoElectronico.setText("david@david.es")
        binding.etContrasena.setText("1234")
    }

    private fun setupListeners() = with(binding) {
        btnLogin.setOnClickListener { doLogin() }
        btnRegister.setOnClickListener { Toast.makeText(binding.root.context, "Este botón no hace nada :D", Toast.LENGTH_LONG).show() }
    }

    private fun doLogin() {

        val correo = binding.etCorreoElectronico.text.toString()
        val contrasena = binding.etContrasena.text.toString()

        viewModel.doLogin(correo, contrasena, requireContext()) { usuario ->

            if (usuario is Empleado) {
                val action = LoginFragmentDirections.actionLoginFragmentToMainFragment()
                findNavController().navigate(action)
            } else if (usuario is Entrenador) {
                val action = LoginFragmentDirections.actionLoginFragmentToEntrenadorMainFragment()
                findNavController().navigate(action)
            }

        }

    }

}
