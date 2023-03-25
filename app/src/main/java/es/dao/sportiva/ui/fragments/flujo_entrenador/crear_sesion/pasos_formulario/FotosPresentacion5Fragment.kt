package es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentFotosPresentacion5Binding

class FotosPresentacion5Fragment : Fragment() {

    private lateinit var binding: FragmentFotosPresentacion5Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFotosPresentacion5Binding.inflate(inflater, container, false)
        return binding.root
    }

}