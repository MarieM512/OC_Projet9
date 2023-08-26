package com.openclassrooms.realestatemanager.ui.composant.bottomNavigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.openclassrooms.realestatemanager.theme.AppTheme
import com.openclassrooms.realestatemanager.ui.AddView
import com.openclassrooms.realestatemanager.ui.FilterView
import com.openclassrooms.realestatemanager.ui.ListView
import com.openclassrooms.realestatemanager.ui.MapView
import com.openclassrooms.realestatemanager.ui.loan.LoanScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Map.route) {
        composable(BottomNavItem.Map.route) {
            MapView()
        }
        composable(BottomNavItem.List.route) {
            ListView()
        }
        composable(BottomNavItem.Add.route) {
            AddView()
        }
        composable(BottomNavItem.Filter.route) {
            FilterView()
        }
        composable(BottomNavItem.Loan.route) {
            LoanScreen()
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
