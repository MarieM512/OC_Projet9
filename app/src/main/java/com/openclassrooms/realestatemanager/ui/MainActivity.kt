package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.PropertyDatabase
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.BottomBar
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.NavigationGraph

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PropertyDatabase::class.java,
            "properties.db",
        ).build()
    }

    private val viewModel by viewModels<PropertyViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PropertyViewModel(db.dao) as T
                }
            }
        },
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
//            val state by viewModel.state.collectAsState()
//            PropertyScreen(state = state, onEvent = viewModel::onEvent)
            Scaffold(
                bottomBar = {
                    BottomBar(navController = navController)
                },
            ) { padding ->
                Box(
                    modifier = Modifier.padding(padding),
                ) {
                    NavigationGraph(navController = navController)
                }
            }
        }
    }
}