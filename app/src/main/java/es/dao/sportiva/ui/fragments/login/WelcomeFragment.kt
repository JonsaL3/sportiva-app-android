package es.dao.sportiva.ui.fragments.login

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dxcustomlibrary.visible
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentWelcomeBinding
import es.dao.sportiva.models.empleado.Empleado
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.adapters.RegisterViewPagerAdapter
import es.dao.sportiva.utils.CamaraActivity
import es.dao.sportiva.utils.DxImplementation
import es.dao.sportiva.utils.UiState
import es.dao.sportiva.utils.getFile
import es.dao.sportiva.utils.toBase64
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
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

    private val realizarFotografiaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            registroViewModel.profliePictureUri = result.data?.data.toString()
            Glide.with(requireContext()).load(registroViewModel.profliePictureUri).circleCrop().into(binding.includeRegistroEntrenador.ivFoto)
            binding.includeRegistroEntrenador.tvInicial.visibility = View.GONE
        }
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
            if (countRegisterEntrenador == 5) {
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

    private fun setupInclude() = with(binding.includeRegistroEntrenador) {

        etNombre.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable?) {
                tvInicial.visibility = View.VISIBLE
                tvInicial.text = s?.let{
                    if(it.isEmpty())
                        "-"
                    else
                        it[0].uppercase()
                }?:run{"-"}
            }
        })

        photoButton.setOnClickListener {
            realizarImagen()
        }

        etFechaNacimiento.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.fecha_de_naciniento))
                    .build()

            datePicker.addOnPositiveButtonClickListener {
                etFechaNacimiento.setText(datePicker.headerText)
                registroViewModel.birthDate = LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(it),
                    ZoneId.systemDefault()
                )
            }

            datePicker.show(requireActivity().supportFragmentManager, "tag")
        }

        registroViewModel.getEmpresas() { listEmpresas ->

            listEmpresas?.let { list ->
                etautoEmpresa.setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_dropdown_item_1line,
                        list.map { it.nombre }
                    ))

                etautoEmpresa.setOnItemClickListener { _, _, position, _ ->
                    registroViewModel.empresa = list[position]
                }
            }

        }

        btnVolverRegistroEntrenador.setOnClickListener {
            this@WelcomeFragment.binding.registroEntrenadorLinearLayout.visibility = View.GONE
        }

        btnRegistrarse.setOnClickListener {
            if (checkDataIdEmpty()) {
                Toast.makeText(requireContext(), "Rellene todos los campos", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {

                val onError = {
                    uiState.setError(requireContext().getString(R.string.error_al_registrarse))
                }

                val onSuccses = {
                    uiState.setSuccess()
                    viewModel.setUsuerRegister(registroViewModel.entrenador) {
                        val action =
                            WelcomeFragmentDirections.actionLoginFragmentToEntrenadorMainFragment3()
                        findNavController().navigate(action)
                    }

                    //reset UI
                    etCorreo.setText("")
                    etContrasenia.setText("")
                    etNombre.setText("")
                    etApellido1.setText("")
                    etApellido2.setText("")
                    etEstudios.setText("")
                    etSueldo.setText("")
                    etautoEmpresa.setText("")

                    Unit
                }

                val onFailure = {
                    uiState.setError(requireContext().getString(R.string.error_al_registrarse))
                }

                uiState.setLoadingFullScreen(
                    requireContext().getString(R.string.registrando_entrenador),
                    requireContext().getString(R.string.estamos_registrando_tu_cuenta_por_favor_espera_un_momento)
                )

                registroViewModel.entrenador = Entrenador(
                    id = 0,
                    correo = etCorreo.text.toString(),
                    contrasena = etContrasenia.text.toString(),
                    nombre = etNombre.text.toString(),
                    apellido1 = etApellido1.text.toString(),
                    apellido2 = etApellido2.text.toString(),
                    fechaNacimiento = registroViewModel.birthDate?: LocalDateTime.now(),
                    fechaInserccion = LocalDateTime.now(),
                    isActivo = true,
                    imagen = null,
                    estudios = etEstudios.text.toString(),
                    sueldo = etSueldo.text.toString().toFloat(),
                    fechaAlta = LocalDateTime.now(),
                    empresaAsignada = registroViewModel.empresa,
                    isCurrentSesion = false,
                    isSeleccionadoParaSerParticipante = false
                )

                viewLifecycleOwner.lifecycleScope.launch{
                    try{
                        val uriImagen = Uri.parse(registroViewModel.profliePictureUri)
                        val file = uriImagen.getFile(requireContext())
                        val compressedFile = Compressor.compress(requireContext(), file) {
                            resolution(1280, 720)
                            quality(50)
                            format(Bitmap.CompressFormat.JPEG)
                        }

                        registroViewModel.entrenador.imagen = compressedFile.toUri().toBase64(requireContext())
                    }catch (e: Exception) {
                        e.printStackTrace()
                    }
                    registroViewModel.registerEntrenador(onError, onSuccses, onFailure)
                }
            }
        }
    }

    private fun checkDataIdEmpty(): Boolean = with(binding.includeRegistroEntrenador) {
        return@with etNombre.text.toString().isEmpty() ||
                etApellido1.text.toString().isEmpty() ||
                etCorreo.text.toString().isEmpty() ||
                etContrasenia.text.toString().isEmpty() ||
                etApellido2.text.toString().isEmpty() ||
                etEstudios.text.toString().isEmpty() ||
                etSueldo.text.toString().isEmpty() ||
                etautoEmpresa.text.toString().isEmpty()
    }

    private fun realizarImagen() {
        val intent = Intent(requireContext(), CamaraActivity::class.java)
        realizarFotografiaResult.launch(intent)
    }
}