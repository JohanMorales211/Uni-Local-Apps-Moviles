package com.example.unilocal.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unilocal.activities.ForgotPassActivity
import com.example.unilocal.databinding.FragmentSettingsBinding
import com.example.unilocal.ui.login.LoginActivity
import com.example.unilocal.utils.language
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    lateinit var  binding: FragmentSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            requireActivity().startActivity( intent )
            requireActivity().finish()

        }
        binding.buttonChangePass.setOnClickListener {
            goToChangePass()
        }
        binding.buttonChangeLanguage.setOnClickListener {
            changeLanguage()
        }

        return binding.root
    }

    private fun goToChangePass() {
        val intent = Intent(context, ForgotPassActivity::class.java)
        startActivity(intent)
    }

    private fun goToLogin(){
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun changeLanguage(){
        language.selectLanguage(requireActivity())
        val intent = activity?.intent
        if (intent != null) {
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            activity?.finish()
            activity?.startActivity(intent)
        }

    }

}