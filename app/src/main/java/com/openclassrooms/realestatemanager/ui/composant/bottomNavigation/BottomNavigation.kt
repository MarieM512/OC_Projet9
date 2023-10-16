package com.openclassrooms.realestatemanager.ui.composant.bottomNavigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.entity.Property
import com.openclassrooms.realestatemanager.database.PropertyState
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.add.AddScreen
import com.openclassrooms.realestatemanager.ui.detail.DetailScreen
import com.openclassrooms.realestatemanager.ui.filter.FilterScreen
import com.openclassrooms.realestatemanager.ui.list.ListScreen
import com.openclassrooms.realestatemanager.ui.list.ListTabletScreen
import com.openclassrooms.realestatemanager.ui.loan.LoanScreen
import com.openclassrooms.realestatemanager.ui.map.MapScreen

@Composable
fun NavigationGraph(
    navController: NavHostController,
    state: PropertyState,
    viewModel: PropertyViewModel,
    windowSizeClass: WindowSizeClass,
) {
    NavHost(navController, startDestination = BottomNavItem.Map.route) {
        composable(BottomNavItem.Map.route) {
            MapScreen(state, navController)
        }
        composable(BottomNavItem.List.route) {
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    ListScreen(state, navController, windowSizeClass, viewModel)
                } else -> {
                    ListTabletScreen(state, navController, windowSizeClass, viewModel)
                }
            }
        }
        composable(BottomNavItem.Add.route) {
            AddScreen(state = state, viewModel = viewModel, navController = navController)
        }
        composable(BottomNavItem.Filter.route) {
            FilterScreen(state, viewModel::onEvent)
        }
        composable(BottomNavItem.Loan.route) {
            LoanScreen()
        }
        composable("property/{propertyId}") { navBackStackEntry ->
            val property = navBackStackEntry.arguments?.getString("propertyId")?.let { Gson().fromJson(it, Property::class.java) }
            if (property != null) {
                DetailScreen(property = property, navController = navController, nearInterestPointList = viewModel.getNearInterestPoint(property.id), viewModel = viewModel)
            }
        }
        composable("edit/{propertyId}") { navBackStackEntry ->
            val property = navBackStackEntry.arguments?.getString("propertyId")?.let { Gson().fromJson(it, Property::class.java) }
            if (property != null) {
                AddScreen(state = state, viewModel = viewModel, property = property, navController = navController)
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
) {
    AppTheme {
        val views = listOf(
            BottomNavItem.Map,
            BottomNavItem.List,
            BottomNavItem.Add,
            BottomNavItem.Filter,
            BottomNavItem.Loan,
        )
        NavigationBar(
            modifier = Modifier,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            views.forEach { view ->
                NavigationBarItem(
                    label = {
                        Text(text = view.title)
                    },
                    icon = {
                        Icon(painterResource(id = view.icon), contentDescription = "")
                    },
                    selected = currentRoute == view.route,
                    onClick = {
                        navController.navigate(view.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                )
            }
        }
    }
}
