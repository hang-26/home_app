package com.example.homeapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.homeapp.R
import com.example.homeapp.data.StatusDataClass
import com.example.homeapp.databinding.FragmentActiveBinding


class ActiveFragment : Fragment() {

    lateinit var bindding: FragmentActiveBinding

    lateinit var myActiveAdapter: FragmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bindding = FragmentActiveBinding.inflate(layoutInflater, container, false)
        myActiveAdapter = FragmentAdapter(this)
        bindding.viewPager2.adapter = myActiveAdapter

//        sendDataToFragment(PostedFragment())
        bindding.navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.itPosted -> {
                    bindding.viewPager2.setCurrentItem(0, false)
                }

                R.id.itWorked -> {
                    bindding.viewPager2.setCurrentItem(1, false)
                }
            }
            true
        }

        return bindding.root
    }

}