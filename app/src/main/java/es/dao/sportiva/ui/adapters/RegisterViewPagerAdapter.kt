package es.dao.sportiva.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import es.dao.sportiva.R
import es.dao.sportiva.ui.MainActivity
import es.dao.sportiva.ui.fragments.login.RegisterFragment

class RegisterViewPagerAdapter(
    activity: MainActivity,
) : FragmentStateAdapter(
    activity
) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RegisterFragment(
                resourceUndraw = R.drawable.undraw_events_re_98ue,
                resourceStringContentDescription = R.string.lleva_control_de_las_sesiones
            )
            1 -> RegisterFragment(
                resourceUndraw = R.drawable.undraw_home_run_hski,
                resourceStringContentDescription = R.string.consigue_tus_objectivos
            )
            else -> RegisterFragment(
                resourceUndraw = R.drawable.undraw_fitness_tracker_3033,
                resourceStringContentDescription = R.string.palma_mano
            )
        }
    }

}