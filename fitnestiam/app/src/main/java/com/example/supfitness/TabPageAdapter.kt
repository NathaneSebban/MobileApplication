package com.example.supfitness

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabPageAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 5;
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return WeightTracker()
            1 -> return HistoryFragment()
            2 -> return  FootRacesTwoFragment()
            3 -> return  HistoryFragmentTwo()
            4 -> return  FootRacesFragment()
            else -> return WeightTracker()
        }
    }
}