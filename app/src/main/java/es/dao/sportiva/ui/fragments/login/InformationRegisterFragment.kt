package es.dao.sportiva.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import es.dao.sportiva.R
import es.dao.sportiva.databinding.FragmentInformationRegisterBinding

class InformationRegisterFragment(
    @DrawableRes val resourceUndraw: Int,
    @StringRes val resourceStringContentDescription: Int
) : Fragment() {

    lateinit var binding: FragmentInformationRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgvUndraw.setImageResource(resourceUndraw)
        binding.txtDescription.text = getString(resourceStringContentDescription)

    }

}