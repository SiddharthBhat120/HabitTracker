package com.digitalsanctuary.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.digitalsanctuary.habittracker.navigation.NavGraph
import com.digitalsanctuary.habittracker.ui.theme.HabitTrackerTheme
import com.digitalsanctuary.habittracker.viewmodel.AuthViewModel
import com.digitalsanctuary.habittracker.viewmodel.HabitViewModel
import com.digitalsanctuary.habittracker.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            val habitViewModel: HabitViewModel = viewModel()
            val settings by settingsViewModel.state.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            HabitTrackerTheme(darkTheme = settings.darkMode) {
                NavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    habitViewModel = habitViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}
