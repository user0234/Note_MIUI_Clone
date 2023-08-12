package com.hellow.notemiuiclone.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.hellow.notemiuiclone.ui.fragment.NoteFragment
import com.hellow.notemiuiclone.ui.fragment.TaskFragment


class NoteTabAdaptor(
    private val myContext: Context,
    fm: FragmentManager,
    private var totalTabs: Int,
) : FragmentPagerAdapter(fm) {


    override fun getCount(): Int {
        return totalTabs
    }

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                NoteFragment()
            }

            1 -> {
                TaskFragment()
            }

            else -> {
                NoteFragment()
            }
        }
    }
}