package es.dao.sportiva.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.pasos_formulario.ConfirmarAsistencia2Fragment
import es.dao.sportiva.ui.fragments.flujo_entrenador.comenzar_sesion.pasos_formulario.SeleccionarSesion1Fragment

class ComenzarSesionViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(
    fragmentManager,
    lifecycle
) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SeleccionarSesion1Fragment()
            else -> ConfirmarAsistencia2Fragment()
        }
    }

}