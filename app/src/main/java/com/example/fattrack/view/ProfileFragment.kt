package com.example.fattrack.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fattrack.R
import com.example.fattrack.databinding.FragmentProfileBinding
import com.example.fattrack.view.profile.EditProfileFragment

class ProfileFragment : Fragment() {
    private var _bindingProfile: FragmentProfileBinding? = null
    private val bindingProfile get() = _bindingProfile!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        //init binding
        _bindingProfile = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = bindingProfile.root


        //button edit profile
        bindingProfile.btnEditProfile.setOnClickListener {
            //move to edit profile fragment
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.nav_host, EditProfileFragment())
            transaction.addToBackStack(null) // add ke back stack
            transaction.commit()
        }

        return root
    }


}