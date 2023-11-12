package com.example.unilocal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.viewpager.widget.ViewPager
import com.example.unilocal.R
import com.example.unilocal.adapter.ViewPagerAdapter
import com.example.unilocal.databinding.ActivityModeratorBinding
import com.example.unilocal.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class ModeratorActivity : AppCompatActivity() {

    lateinit var binding:ActivityModeratorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityModeratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = ViewPagerAdapter(this)
        TabLayoutMediator(binding.tabs, binding.viewPager){ tab, pos ->
            when(pos){
                0 -> tab.text = "Lugares Pendientes"
                1 -> tab.text = "Lugares Aceptados"
                2 -> tab.text = "Lugares Rechazados"
            }
        }.attach()

        binding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity( intent )
            finish()
        }
    }

    private fun goToLogin(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}