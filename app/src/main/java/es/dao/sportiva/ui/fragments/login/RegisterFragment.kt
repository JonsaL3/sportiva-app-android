package es.dao.sportiva.ui.fragments.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentInformationRegisterBinding
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.utils.UiState
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment(
    @DrawableRes val resourceUndraw: Int,
    @StringRes val resourceStringContentDescription: Int
) : Fragment() {

    lateinit var binding: FragmentInformationRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    @Inject
    lateinit var uiState: UiState

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            imgvUndraw.setImageResource(resourceUndraw)
            txtDescription.text = getString(resourceStringContentDescription)

            btnUnirme.setOnClickListener {
                showRegistrationForm()
            }
        }

    }

    override fun onResume() {
        super.onResume()

        if (resourceUndraw == R.drawable.undraw_fitness_tracker_3033) {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.btnUnirme.visibility = View.VISIBLE
            }, 100)
        }
    }

    override fun onPause() = with(binding) {
        super.onPause()

        btnUnirme.apply {
            visibility = View.INVISIBLE
            text = getString(R.string.unirme)
            setOnClickListener {
                showRegistrationForm()
            }
        }

        if (resourceUndraw == R.drawable.undraw_fitness_tracker_3033) {

            imgvUndraw.animate().translationX(0f).setDuration(0).start()
            txtDescription.animate().translationX(-0f).setDuration(0).start()
            txtDescriptionRegistro.animate().translationX(1000f).setDuration(0).start()
            textInputLayoutEmail.animate().translationX(1000f).setDuration(0).start()
            textInputLayoutPassword.animate().translationX(1000f).setDuration(0).start()
            btnContinuar.animate().translationX(1000f).setDuration(0).start()

            textInputLayoutNombre.animate().translationX(1000f).setDuration(0).start()
            textInputLayoutApellido1.animate().translationX(1000f).setDuration(0).start()
            textInputLayoutApellido2.animate().translationX(1000f).setDuration(0).start()
            textInputLayoutEmpresa.animate().translationX(1000f).setDuration(0).start()

            textInputLayoutCargo.animate().translationX(1000f).setDuration(0).start()
            textInputLayoutAltura.animate().translationX(1000f).setDuration(0).start()
            textInputLayoutPeso.animate().translationX(1000f).setDuration(0).start()
            chbFumador.animate().translationX(1000f).setDuration(0).start()
            chbDeporte.animate().translationX(1000f).setDuration(0).start()
            btnContinuar.text = getString(R.string.continuar)

        }
    }

    private fun showRegistrationForm() = with(binding) {

        btnUnirme.apply {
            animate().alpha(0f).setDuration(250).start()
            postDelayed({
                text = getString(R.string.volver)
                setOnClickListener {
                    (requireActivity() as MainActivity).actionOnBackPressed?.invoke()
                }
                animate().alpha(1f).setDuration(500).start()
            }, 250)
        }

        btnContinuar.setOnClickListener {
            if (etEmail.text.toString().isNotEmpty() && etPassword.text.toString().isNotEmpty()) {

                viewModel.empleado.contrasena = etPassword.text.toString()
                viewModel.empleado.correo = etEmail.text.toString()

                secondStep()

            } else {
                if (etEmail.text.toString().isEmpty()) Toast.makeText(
                    requireContext(),
                    "Introduce un email",
                    Toast.LENGTH_SHORT
                ).show()
                if (etPassword.text.toString().isEmpty()) Toast.makeText(
                    requireContext(),
                    "Introduce una contraseña",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        imgvUndraw.animate().translationX(-1100f).setDuration(500).start()
        txtDescription.animate().translationX(-1000f).setDuration(600).start()
        txtDescriptionRegistro.animate().translationX(0f).setDuration(600).start()
        textInputLayoutEmail.animate().translationX(0f).setDuration(700).start()
        textInputLayoutPassword.animate().translationX(0f).setDuration(800).start()
        btnContinuar.animate().translationX(0f).setDuration(900).start()
    }

    private fun secondStep() = with(binding){

        txtDescriptionRegistro.apply {
            animate().alpha(0f).setDuration(250).start()

            postDelayed({

                text = getString(R.string.quien_eres)

                animate().alpha(1f).setDuration(500).start()
            }, 250)
        }

        btnContinuar.setOnClickListener {

            if(
                etNombre.text.toString().isNotEmpty() &&
                etApellido1.text.toString().isNotEmpty() &&
                etApellido2.text.toString().isNotEmpty() &&
                etEmpresa.text.toString().isNotEmpty()
            ){
                viewModel.empleado.nombre = etNombre.text.toString()
                viewModel.empleado.apellido1 = etApellido1.text.toString()
                viewModel.empleado.apellido2 = etApellido2.text.toString()
                viewModel.empleado.empresa = Empresa(nombre = etEmpresa.text.toString())

                thirdStep()

            }else{
                Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        textInputLayoutEmail.animate().translationX(-1000f).setDuration(700).start()
        textInputLayoutPassword.animate().translationX(-1000f).setDuration(800).start()

        textInputLayoutNombre.animate().translationX(0f).setDuration(700).start()
        textInputLayoutApellido1.animate().translationX(0f).setDuration(800).start()
        textInputLayoutApellido2.animate().translationX(0f).setDuration(900).start()
        textInputLayoutEmpresa.animate().translationX(0f).setDuration(1000).start()

    }

    private fun thirdStep() = with(binding){

        txtDescriptionRegistro.apply {
            animate().alpha(0f).setDuration(250).start()

            postDelayed({

                text = getString(R.string.cuentanos_un_poco_mas)

                animate().alpha(1f).setDuration(500).start()
            }, 250)
        }

        btnContinuar.apply {
            animate().alpha(0f).setDuration(250).start()
            postDelayed({
                text = getString(R.string.grito_registrarme)
                setOnClickListener {

                    if(
                        etCargo.text.toString().isNotEmpty() &&
                        etAltura.text.toString().isNotEmpty() &&
                        etPeso.text.toString().isNotEmpty()
                    ){

                        viewModel.empleado.cargo = etCargo.text.toString()
                        viewModel.empleado.altura = etAltura.text.toString().toFloat()
                        viewModel.empleado.peso = etPeso.text.toString().toFloat()
                        viewModel.empleado.isFumador = chbFumador.isChecked
                        viewModel.empleado.isDeporteFrecuente = chbDeporte.isChecked

                        val onError = {
                            uiState.setError(context.getString(R.string.error_al_registrarse))
                        }

                        val onSuccses = {
                            uiState.setSuccess()
                            //TODO
//                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }

                        val onFailure = {
                            uiState.setError(context.getString(R.string.error_al_registrarse))
                        }

                        uiState.setLoadingFullScreen(
                            context.getString(R.string.registrando_empleado),
                            context.getString(R.string.estamos_registrando_tu_cuenta_por_favor_espera_un_momento)
                        )
                        viewModel.registerEmpleado(onError, onSuccses, onFailure)
                    } else{
                        Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                    }

                }
                animate().alpha(1f).setDuration(500).start()
            }, 250)
        }

        textInputLayoutNombre.animate().translationX(-1000f).setDuration(700).start()
        textInputLayoutApellido1.animate().translationX(-1000f).setDuration(800).start()
        textInputLayoutApellido2.animate().translationX(-1000f).setDuration(900).start()
        textInputLayoutEmpresa.animate().translationX(-1000f).setDuration(1000).start()

        textInputLayoutCargo.animate().translationX(0f).setDuration(700).start()
        textInputLayoutAltura.animate().translationX(0f).setDuration(800).start()
        textInputLayoutPeso.animate().translationX(0f).setDuration(900).start()
        chbFumador.animate().translationX(0f).setDuration(1000).start()
        chbDeporte.animate().translationX(0f).setDuration(1100).start()

    }
}