package com.hellow.notemiuiclone.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.radiobutton.MaterialRadioButton
import com.hellow.notemiuiclone.R
import com.hellow.notemiuiclone.databinding.FragmentSettingsBinding
import com.hellow.notemiuiclone.ui.mainActivity.MainActivity
import com.hellow.notemiuiclone.ui.mainActivity.MainActivityViewModel
import com.hellow.notemiuiclone.utils.sharedPref.settings.LayoutTypeEnum
import com.hellow.notemiuiclone.utils.sharedPref.settings.SettingsSharedPref.getSharedPrefNotesLayout
import com.hellow.notemiuiclone.utils.sharedPref.settings.SettingsSharedPref.getSharedPrefNotesSort
import com.hellow.notemiuiclone.utils.sharedPref.settings.SettingsSharedPref.updateSharedPrefNotesLayout
import com.hellow.notemiuiclone.utils.sharedPref.settings.SettingsSharedPref.updateSharedPrefNotesSort
import com.hellow.notemiuiclone.utils.sharedPref.settings.SortTypeEnum
import com.hellow.notemiuiclone.utils.toDip


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding


    private val viewModel: MainActivityViewModel by lazy {

        (activity as MainActivity).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        setUpLayoutRadioGroup()
        setUpSortRadioGroup()

    }

    private fun setUpSortRadioGroup() {
        val name = MaterialRadioButton(requireContext())
        name.id = R.id.sortByName

        name.setPadding(requireContext().toDip(25).toInt(), 0, 0, 0)

        name.text = "Name"
        name.textSize = 17f
        name.isChecked = true
        binding.sortByRadioGroup.addView(name)

        val createdDate = MaterialRadioButton(requireContext())
        createdDate.id = R.id.sortByCreationDate

        createdDate.setPadding(requireContext().toDip(25).toInt(), 0, 0, 0)

        createdDate.text = "Creation Date"
        createdDate.textSize = 17f
        createdDate.isChecked = true
        binding.sortByRadioGroup.addView(createdDate)

        val modificationDate = MaterialRadioButton(requireContext())
        modificationDate.id = R.id.sortByModificationDate

        modificationDate.setPadding(requireContext().toDip(25).toInt(), 0, 0, 0)

        modificationDate.text = "Modification Date"
        modificationDate.textSize = 17f
        modificationDate.isChecked = true
        binding.sortByRadioGroup.addView(modificationDate)


        val currentPosSet = getSharedPrefNotesSort(requireActivity())
        Log.i("sortSharedPref", "shared pref value - ${currentPosSet.name}")

        when (currentPosSet) {
            SortTypeEnum.Name -> binding.sortByRadioGroup.check(name.id)
            SortTypeEnum.Creation -> binding.sortByRadioGroup.check(createdDate.id)
            SortTypeEnum.Modification -> binding.sortByRadioGroup.check(modificationDate.id)
        }


        binding.sortByRadioGroup.setOnCheckedChangeListener { _, pos ->
            Log.i("sortSharedPref", "current selected position - ${pos}")

            when (pos) {
                name.id -> {
                    updateSharedPrefNotesSort(requireContext(), SortTypeEnum.Name)
                }

                createdDate.id -> {
                    updateSharedPrefNotesSort(requireContext(), SortTypeEnum.Creation)
                }

                modificationDate.id -> {
                    updateSharedPrefNotesSort(requireContext(), SortTypeEnum.Modification)
                }
            }

            viewModel.triggerSharedPrefSortEvent()

        }
    }

    private fun setUpLayoutRadioGroup() {
        val list = MaterialRadioButton(requireContext())
        list.id = R.id.layoutList

        list.setPadding(requireContext().toDip(25).toInt(), 0, 0, 0)

        list.text = "List"
        list.textSize = 17f
        list.isChecked = true
        binding.layoutRadioGroup.addView(list)

        val grid = MaterialRadioButton(requireContext())
        grid.id = R.id.layoutGrid

        grid.setPadding(requireContext().toDip(25).toInt(), 0, 0, 0)

        grid.text = "Grid"
        grid.textSize = 17f
        grid.isChecked = true
        binding.layoutRadioGroup.addView(grid)

        val currentPosSet = getSharedPrefNotesLayout(requireActivity())

        when (currentPosSet) {
            LayoutTypeEnum.List -> binding.layoutRadioGroup.check(list.id)
            LayoutTypeEnum.Grid -> binding.layoutRadioGroup.check(grid.id)
        }

        binding.layoutRadioGroup.setOnCheckedChangeListener { _, pos ->
            viewModel.triggerSharedPrefLayoutEvent()

            when (pos) {
                list.id -> {
                    updateSharedPrefNotesLayout(requireContext(), LayoutTypeEnum.List)
                }

                grid.id -> {
                    updateSharedPrefNotesLayout(requireContext(), LayoutTypeEnum.Grid)
                }
            }
        }
    }


}