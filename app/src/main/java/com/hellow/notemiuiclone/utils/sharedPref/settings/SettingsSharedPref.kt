package com.hellow.notemiuiclone.utils.sharedPref.settings

import android.content.Context
import com.hellow.notemiuiclone.utils.sharedPref.SharedPrefFunctions

object SettingsSharedPref {

    private const val SORT_SHARED_PREF_TAG = "shared_pref_settings"

    /**
     * sort shared pref functions
     */

    // sort converter function

    fun fromSortEnumTOPref(value: SortTypeEnum): Int {
        return when (value) {
            SortTypeEnum.Name -> 0
            SortTypeEnum.Creation -> 1
            SortTypeEnum.Modification -> 2
        }
    }

    fun fromPrefToSortEnum(value: Int): SortTypeEnum {
        return when (value) {
            0 -> SortTypeEnum.Name
            1 -> SortTypeEnum.Creation
            2 -> SortTypeEnum.Modification
            else -> {
                SortTypeEnum.Modification
            }
        }
    }

    // valid value - 0(name),1(creation),2(modification) modification is default

    fun getSharedPrefNotesSort(context: Context): SortTypeEnum {
        val sharePref = SharedPrefFunctions.getSharedPrefInt(
            context,
            SORT_SHARED_PREF_TAG, "notesSort", 2
        )
        return fromPrefToSortEnum(sharePref)
    }

    fun updateSharedPrefNotesSort(context: Context, newValue: SortTypeEnum) {

        val sharedPref = fromSortEnumTOPref(newValue)

        SharedPrefFunctions.putSharedPrefInt(
            context,
            SORT_SHARED_PREF_TAG, "notesSort", sharedPref
        )
    }

    /** layout shared pref
     *
     */

    fun fromLayoutEnumTOPref(value: LayoutTypeEnum): Int {
        return when (value) {
            LayoutTypeEnum.List -> 0
            LayoutTypeEnum.Grid -> 1

        }
    }

    fun fromPrefToLayoutEnum(value: Int): LayoutTypeEnum {
        return when (value) {
            0 -> LayoutTypeEnum.List
            1 -> LayoutTypeEnum.Grid

            else -> {
                LayoutTypeEnum.List
            }
        }
    }

    // valid value - 0(list),1(grid) list is default

    fun getSharedPrefNotesLayout(context: Context): LayoutTypeEnum {
        val sharePref = SharedPrefFunctions.getSharedPrefInt(
            context,
            SORT_SHARED_PREF_TAG, "notesLayout", 0
        )
        return fromPrefToLayoutEnum(sharePref)
    }

    fun updateSharedPrefNotesLayout(context: Context, newValue: LayoutTypeEnum) {

        val sharedPref = fromLayoutEnumTOPref(newValue)

        SharedPrefFunctions.putSharedPrefInt(
            context,
            SORT_SHARED_PREF_TAG, "notesLayout", sharedPref
        )
    }


}