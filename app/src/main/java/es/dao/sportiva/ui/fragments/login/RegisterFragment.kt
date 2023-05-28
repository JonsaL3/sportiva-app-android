package es.dao.sportiva.ui.fragments.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentInformationRegisterBinding
import es.dao.sportiva.models.Empresa
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.utils.CamaraActivity
import es.dao.sportiva.utils.UiState
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment(
    @DrawableRes val resourceUndraw: Int,
    @StringRes val resourceStringContentDescription: Int
) : Fragment() {

    lateinit var binding: FragmentInformationRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val viewModelMain: MainViewModel by activityViewModels()

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

            imgvUndraw.apply {
                animate().translationX(0f).setDuration(0).start()
                alpha = 1f
                visibility = View.VISIBLE
            }
            txtDescription.apply {
                animate().translationX(-0f).setDuration(0).start()
                alpha = 1f
                visibility = View.VISIBLE
            }
            txtDescriptionRegistro.animate().translationX(1000f).setDuration(0).start().also {
                txtDescriptionRegistro.visibility = View.GONE
                txtDescriptionRegistro.text = null
            }
            textInputLayoutEmail.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutEmail.visibility = View.GONE
                etEmail.text = null
            }
            textInputLayoutPassword.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutPassword.visibility = View.GONE
                etPassword.text = null
            }
            btnContinuar.animate().translationX(1000f).setDuration(0).start().also {
                btnContinuar.visibility = View.GONE
            }
            textInputLayoutNombre.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutNombre.visibility = View.GONE
                etNombre.text = null
            }
            textInputLayoutApellido1.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutApellido1.visibility = View.GONE
                etApellido1.text = null
            }
            textInputLayoutApellido2.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutApellido2.visibility = View.GONE
                etApellido2.text = null
            }
            textInputLayoutEmpresa.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutEmpresa.visibility = View.GONE
                etEmpresa.text = null
            }
            textInputLayoutCargo.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutCargo.visibility = View.GONE
                etCargo.text = null
            }
            textInputLayoutAltura.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutAltura.visibility = View.GONE
                etAltura.text = null
            }
            textInputLayoutPeso.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutPeso.visibility = View.GONE
                etPeso.text = null
            }
            chbFumador.animate().translationX(1000f).setDuration(0).start().also {
                chbFumador.visibility = View.GONE
                chbFumador.isChecked = false
            }
            chbDeporte.animate().translationX(1000f).setDuration(0).start().also {
                chbDeporte.visibility = View.GONE
                chbDeporte.isChecked = false
            }
            textInputLayoutFechaNacimiento.animate().translationX(1000f).setDuration(0).start().also {
                textInputLayoutFechaNacimiento.visibility = View.GONE
                etFechaNacimiento.text = null
            }

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

                viewModel.empleado.apply {
                    contrasena = etPassword.text.toString()
                    correo = etEmail.text.toString()
                    fechaNacimiento = viewModel.birthDate?: LocalDateTime.now()
                }

                secondStep()

            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.rellena_todos_loas_campos),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        etFechaNacimiento.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.fecha_de_naciniento))
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                etFechaNacimiento.setText(datePicker.headerText)
                viewModel.birthDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneId.systemDefault()
                )
            }

            datePicker.show(requireActivity().supportFragmentManager, "tag")
        }

        imgvUndraw.apply {
            animate().translationX(-1100f).setDuration(500).start()
            animate().alpha(0f).setDuration(500) .start()
        }

        txtDescription.apply {
            animate().translationX(-1000f).setDuration(600).start()
            animate().alpha(0f).setDuration(600).start()
        }

        txtDescriptionRegistro.apply {
            animate().translationX(0f).setDuration(600).start()
            animate().alpha(1f).setDuration(300).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 300)
        }

        textInputLayoutEmail.apply {
            animate().translationX(0f).setDuration(700).start()
            animate().alpha(1f).setDuration(350).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 350)
        }

        textInputLayoutPassword.apply {
            animate().translationX(0f).setDuration(800).start()
            animate().alpha(1f).setDuration(400).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 400)
        }

        btnContinuar.apply {
            animate().translationX(0f).setDuration(900).start()
            animate().alpha(1f).setDuration(450).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 450)
        }

        textInputLayoutFechaNacimiento.apply {
            animate().translationX(0f).setDuration(1000).start()
            animate().alpha(1f).setDuration(500).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 500)
        }
    }

    private fun secondStep() = with(binding){

        viewModel.getEmpresas(){ listEmpresas ->

            listEmpresas?.let{ list ->
                binding.etEmpresa.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        list.map { it.nombre }
                    ))

                binding.etEmpresa.setOnItemClickListener { parent, view, position, id ->
                    viewModel.empresa = list[position]
                }
            }

        }

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
                viewModel.empleado.apply {
                    nombre = etNombre.text.toString()
                    apellido1 = etApellido1.text.toString()
                    apellido2 = etApellido2.text.toString()
                    empresa = Empresa(nombre = etEmpresa.text.toString())
                }

                thirdStep()

            }else{
                Toast.makeText(requireContext(), getString(R.string.rellena_todos_loas_campos), Toast.LENGTH_SHORT).show()
            }
        }

        textInputLayoutEmail.apply {
            animate().translationX(-1000f).setDuration(700).start()
            animate().alpha(0f).setDuration(350).start()
            postDelayed({
                visibility = View.GONE
            }, 350)
        }

        textInputLayoutPassword.apply {
            animate().translationX(-1000f).setDuration(800).start()
            animate().alpha(0f).setDuration(400).start()
            postDelayed({
                visibility = View.GONE
            }, 400)
        }

        textInputLayoutFechaNacimiento.apply {
            animate().translationX(-1000f).setDuration(900).start()
            animate().alpha(0f).setDuration(450).start()
            postDelayed({
                visibility = View.GONE
            }, 450)
        }

        textInputLayoutNombre.apply {
            animate().translationX(0f).setDuration(700).start()
            animate().alpha(1f).setDuration(350).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 350)
        }

        textInputLayoutApellido1.apply {
            animate().translationX(0f).setDuration(800).start()
            animate().alpha(1f).setDuration(400).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 400)
        }

        textInputLayoutApellido2.apply {
            animate().translationX(0f).setDuration(900).start()
            animate().alpha(1f).setDuration(450).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 450)
        }

        textInputLayoutEmpresa.apply {
            animate().translationX(0f).setDuration(1000).start()
            animate().alpha(1f).setDuration(500).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 500)
        }

    }

    private fun thirdStep() = with(binding){

        txtDescriptionRegistro.apply {
            animate().alpha(0f).setDuration(250).start()

            postDelayed({

                text = getString(R.string.cuentanos_un_poco_mas)

                animate().alpha(1f).setDuration(500).start()
            }, 250)
        }

        chbFumador.setOnCheckedChangeListener{ _, isChecked ->
            viewModel.empleado.isFumador = chbFumador.isChecked
        }

        chbDeporte.setOnCheckedChangeListener{ _, isChecked ->
            viewModel.empleado.isDeporteFrecuente = chbDeporte.isChecked
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

                        val onError = {
                            uiState.setError(context.getString(R.string.error_al_registrarse))
                        }

                        val onSuccses = {
                            uiState.setSuccess()
                            viewModelMain.setUsuerRegister(viewModel.empleado){
                                val action =
                                    WelcomeFragmentDirections.actionLoginFragmentToEmpleadoMainFragment()
                                findNavController().navigate(action)
                            }

                            Unit
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

        textInputLayoutNombre.apply {
            animate().translationX(-1000f).setDuration(700).start()
            animate().alpha(0f).setDuration(350).start()
            postDelayed({
                visibility = View.GONE
            }, 350)
        }

        textInputLayoutApellido1.apply {
            animate().translationX(-1000f).setDuration(800).start()
            animate().alpha(0f).setDuration(400).start()
            postDelayed({
                visibility = View.GONE
            }, 400)
        }

        textInputLayoutApellido2.apply {
            animate().translationX(-1000f).setDuration(900).start()
            animate().alpha(0f).setDuration(450).start()
            postDelayed({
                visibility = View.GONE
            }, 450)
        }

        textInputLayoutEmpresa.apply {
            animate().translationX(-1000f).setDuration(1000).start()
            animate().alpha(0f).setDuration(500).start()
            postDelayed({
                visibility = View.GONE
            }, 500)
        }

        textInputLayoutCargo.apply {
            animate().translationX(0f).setDuration(700).start()
            animate().alpha(1f).setDuration(350).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 350)
        }

        textInputLayoutAltura.apply {
            animate().translationX(0f).setDuration(800).start()
            animate().alpha(1f).setDuration(400).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 400)
        }

        textInputLayoutPeso.apply {
            animate().translationX(0f).setDuration(900).start()
            animate().alpha(1f).setDuration(450).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 450)
        }

        chbFumador.apply {
            animate().translationX(0f).setDuration(1000).start()
            animate().alpha(1f).setDuration(500).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 500)
        }

        chbDeporte.apply {
            animate().translationX(0f).setDuration(1100).start()
            animate().alpha(1f).setDuration(550).start()
            postDelayed({
                visibility = View.VISIBLE
            }, 550)
        }
    }
}