package es.dao.sportiva.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario.Aforo4Fragment
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario.AnadirEntrenadores1Fragment
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario.FechaYHora3Fragment
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario.FotosPresentacion5Fragment
import es.dao.sportiva.ui.fragments.flujo_entrenador.crear_sesion.pasos_formulario.InformacionGeneral2Fragment

class CrearSesionViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(
    fragmentManager,
    lifecycle
) {
    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AnadirEntrenadores1Fragment()
            1 -> InformacionGeneral2Fragment()
            2 -> FechaYHora3Fragment()
            3 -> Aforo4Fragment()
            else -> FotosPresentacion5Fragment()
        }
    }

}