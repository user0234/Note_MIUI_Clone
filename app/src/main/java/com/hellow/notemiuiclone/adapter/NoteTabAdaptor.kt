package com.hellow.notemiuiclone.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hellow.notemiuiclone.ui.fragment.NoteFragment
import com.hellow.notemiuiclone.ui.fragment.TaskFragment


class NoteTabAdaptor(
    fragmentActivity: FragmentActivity,
    private var totalTabs: Int,
) : FragmentStateAdapter(fragmentActivity) {


    override fun getItemCount(): Int {
        return totalTabs
    }

    // this is for fragment tabs
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {

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