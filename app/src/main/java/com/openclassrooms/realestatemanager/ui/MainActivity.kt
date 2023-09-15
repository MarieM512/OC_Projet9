package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.PropertyDatabase
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.BottomBar
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.NavigationGraph
import com.openclassrooms.realestatemanager.utils.Converters

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            PropertyDatabase::class.java,
            "properties.db",
        )
            .addTypeConverter(Converters())
            .build()
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

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val state by viewModel.state.collectAsState()
            val windowSizeClass = calculateWindowSizeClass(activity = this)
//            deleteDatabase("properties.db")
//            PropertyScreen(state = state, onEvent = viewModel::onEvent)
            Scaffold(
                bottomBar = {
                    BottomBar(navController = navController)
                },
            ) { padding ->
                Box(
                    modifier = Modifier.padding(padding),
                ) {
                    NavigationGraph(navController = navController, state = state, viewModel = viewModel, windowSizeClass = windowSizeClass)
                }
            }
        }
    }
}
