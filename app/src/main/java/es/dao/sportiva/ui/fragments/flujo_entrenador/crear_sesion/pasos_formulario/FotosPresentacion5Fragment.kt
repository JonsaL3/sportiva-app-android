package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentFotosPresentacion5Binding
import es.dao.sportiva.ui.MainViewModel
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorState
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.CrearSesionEntrenadorViewModel
import es.dao.sportiva.utils.CamaraActivity
import es.dao.sportiva.utils.getBitmap

class FotosPresentacion5Fragment : Fragment() {

    private lateinit var binding: FragmentFotosPresentacion5Binding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: CrearSesionEntrenadorViewModel by activityViewModels()

    private val seleccionarArchivoResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val image = result.data?.data
            viewModel.setUriFromDispositivo(image.toString())
        }
    }

    private val realizarFotografiaResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val image = result.data?.data
            viewModel.setUriRecienRealizada(image.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFotosPresentacion5Binding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupImageListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setState(CrearSesionEntrenadorState.Neutral)
    }

    private fun setupImageListeners() {

        viewModel.uriFromDispositivo.observe(viewLifecycleOwner) {
            val uri = Uri.parse(it)
            binding.sivImagenSeleccionada.setImageBitmap(uri.getBitmap(requireContext()))
        }

        viewModel.uriRecienRealizada.observe(viewLifecycleOwner) {
            val uri = Uri.parse(it)
            binding.sivFotoRealizada.setImageBitmap(uri.getBitmap(requireContext()))
        }

    }
    private fun setupClickListeners() = with(binding) {

        cvSeleccioneImagen.setOnClickListener {
            seleccionarArchivo()
        }
        cvRealizarImagen.setOnClickListener {
            realizarImagen()
        }

    }

    private fun seleccionarArchivo() {
        seleccionarArchivoResult.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        })
    }

    private fun realizarImagen() {
        val intent = Intent(requireContext(), CamaraActivity::class.java)
        realizarFotografiaResult.launch(intent)
    }

}