package com.example.homeapp

import android.graphics.PostProcessor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.homeapp.databinding.ActivityMainBinding
import com.example.homeapp.fragment.AccountFragment
import com.example.homeapp.fragment.ActiveFragment
import com.example.homeapp.fragment.HomeFragment
import com.example.homeapp.fragment.NotifyFragment
import com.example.homeapp.fragment.PostFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    var auth = Firebase.auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sendDataToFragment(HomeFragment())
        reload()
        binding.navigationBottom.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.itHome -> sendDataToFragment(HomeFragment())
                R.id.itList -> sendDataToFragment(ActiveFragment())
                R.id.itAccount -> sendDataToFragment(AccountFragment())
                R.id.itNotify -> sendDataToFragment(NotifyFragment())
                R.id.itPost -> sendDataToFragment(PostFragment())
                else -> {
                }

            }

            true
        }


    }

    private  fun sendDataToFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransacion = fragmentManager.beginTransaction()

        fragmentTransacion.replace(R.id.layoutFragment, fragment)
        fragmentTransacion.commit()
    }

     fun reload() {
        var current = auth.currentUser
        if (current == null) {
            finish()
        }
    }
}