package com.example.homeapp.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentAdapter(fragmentActivity: ActiveFragment) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                return PostedFragment()
            }
            1 -> {
                return WorkedFragment()
            } else -> {return PostedFragment()}

        }
    }
}