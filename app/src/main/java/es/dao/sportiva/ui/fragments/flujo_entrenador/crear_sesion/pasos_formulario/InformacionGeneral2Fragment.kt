package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentInformacionGeneral2Binding

@AndroidEntryPoint
class InformacionGeneral2Fragment : Fragment() {

    private lateinit var binding: FragmentInformacionGeneral2Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformacionGeneral2Binding.inflate(inflater, container, false)
        return binding.root
    }

}