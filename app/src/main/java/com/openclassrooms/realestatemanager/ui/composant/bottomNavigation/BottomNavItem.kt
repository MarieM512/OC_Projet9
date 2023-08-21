package com.openclassrooms.realestatemanager.ui.composant.bottomNavigation

import com.openclassrooms.realestatemanager.R

sealed class BottomNavItem(val title: String, val icon: Int, val route: String) {
    object Map : BottomNavItem("Map", R.drawable.ic_map, "map_view")
    object List : BottomNavItem("List", R.drawable.ic_list, "list_view")
    object Add : BottomNavItem("Add", R.drawable.ic_add, "add_view")
    object Filter : BottomNavItem("Filter", R.drawable.ic_filter, "filter_view")
    object Loan : BottomNavItem("Loan", R.drawable.ic_loan, "loan_view")
}
