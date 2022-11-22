package es.dao.sportiva.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.fragments.flujo_empleado.EntrenadoresFragment
import es.dao.sportiva.ui.fragments.flujo_empleado.SesionesFragment

class EmpleadoPrincipalViewPagerAdapter(
    activity: MainActivity,
) : FragmentStateAdapter(
    activity
) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SesionesFragment()
            else -> EntrenadoresFragment()
        }
    }

}