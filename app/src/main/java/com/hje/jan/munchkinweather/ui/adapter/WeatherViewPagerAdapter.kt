package com.hje.jan.munchkinweather.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter

class WeatherViewPagerAdapter(
    private val manager: FragmentManager, private val fragments: List<Fragment>
) :
    FragmentStatePagerAdapter(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount() = fragments.size


    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    /*override fun getItemPosition(fragment: Any): Int {
        if (!(fragment as Fragment).isAdded || !fragments.contains(fragment)) {
            return PagerAdapter.POSITION_NONE
        }
        return fragments.indexOf(fragment)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val instantiateItem = super.instantiateItem(container, position) as Fragment
        return if (instantiateItem == fragments[position]) {
            instantiateItem
        } else {
            manager.beginTransaction().add(container.id, fragments[position])
                .commitNowAllowingStateLoss()
            fragments[position]
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, item: Any) {
        val fragment = item as Fragment
        if (fragments.contains(fragment)) {
            super.destroyItem(container, position, item)
            return
        }
        manager.beginTransaction().remove(fragment).commitNowAllowingStateLoss()
    }*/
}