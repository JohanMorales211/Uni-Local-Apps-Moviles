package com.example.unilocal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.unilocal.R
import com.example.unilocal.databinding.FragmentInfoPlaceBinding

class infoPlaceFragment : Fragment() {

    lateinit var binding:FragmentInfoPlaceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInfoPlaceBinding.inflate(inflater, container, false)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}