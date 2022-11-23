package es.dao.sportiva.ui.fragments.flujo_entrenador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentEntrenadorMainBinding

class ComenzarSesionFragment : Fragment() {

    private lateinit var binding: FragmentEntrenadorMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntrenadorMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {

    }

}