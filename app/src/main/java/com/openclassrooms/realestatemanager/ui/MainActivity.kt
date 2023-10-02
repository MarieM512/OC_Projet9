package com.openclassrooms.realestatemanager.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
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
import com.openclassrooms.realestatemanager.ViewModel.PropertyViewModel
import com.openclassrooms.realestatemanager.database.PropertyDatabase
import com.openclassrooms.realestatemanager.database.repository.NearInterestPointRepository
import com.openclassrooms.realestatemanager.database.repository.PictureRepository
import com.openclassrooms.realestatemanager.database.repository.PropertyRepository
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.BottomBar
import com.openclassrooms.realestatemanager.ui.composant.bottomNavigation.NavigationGraph

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<PropertyViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return PropertyViewModel(PropertyRepository(PropertyDatabase.getDatabase(applicationContext).propertyDao), NearInterestPointRepository(PropertyDatabase.getDatabase(applicationContext).nearDao), PictureRepository(PropertyDatabase.getDatabase(applicationContext).pictureDao)) as T
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("success_property", "Property created", NotificationManager.IMPORTANCE_DEFAULT)
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.createNotificationChannel(channel)
            }
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
