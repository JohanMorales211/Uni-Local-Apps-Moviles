package com.example.unilocal.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unilocal.fragment.AcceptedPlacesFragment
import com.example.unilocal.fragment.PendingPlacesFragment
import com.example.unilocal.fragment.RejectedPlacesFragment

class ViewPagerAdapter (var fragment: FragmentActivity): FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> PendingPlacesFragment()
            1 -> AcceptedPlacesFragment()
            else -> RejectedPlacesFragment()
        }
    }
}