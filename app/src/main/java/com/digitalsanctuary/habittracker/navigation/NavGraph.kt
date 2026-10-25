package com.digitalsanctuary.habittracker.navigation

import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.*
import com.digitalsanctuary.habittracker.ui.screens.*
import com.digitalsanctuary.habittracker.viewmodel.*

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    habitViewModel: HabitViewModel,
    settingsViewModel: SettingsViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.Onboarding.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.MainContainer.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
                },
                authViewModel = authViewModel
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinished = {
                    authViewModel.setOnboardingDone()
                    navController.navigate(Screen.Login.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
                }
            )
        }

        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToForgotPassword = { navController.navigate(Screen.ForgotPassword.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.MainContainer.route) { popUpTo(Screen.Login.route) { inclusive = true } }
                },
                authViewModel = authViewModel
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.MainContainer.route) { popUpTo(Screen.Login.route) { inclusive = true } }
                },
                authViewModel = authViewModel
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.MainContainer.route) {
            MainContainer(
                rootNavController = navController,
                authViewModel = authViewModel,
                habitViewModel = habitViewModel,
                settingsViewModel = settingsViewModel
            )
        }

        composable(Screen.CreateHabit.route) {
            CreateHabitScreen(
                onNavigateBack = { navController.popBackStack() },
                onHabitCreated = { navController.popBackStack() },
                habitViewModel = habitViewModel
            )
        }

        composable(
            route = Screen.HabitDetail.route,
            arguments = listOf(navArgument("habitId") { type = NavType.LongType })
        ) { back ->
            HabitDetailScreen(
                habitId = back.arguments?.getLong("habitId") ?: 0L,
                onNavigateBack = { navController.popBackStack() },
                habitViewModel = habitViewModel
            )
        }

        composable(Screen.Journal.route) {
            JournalScreen(
                onNavigateToHome = { navController.navigate(Screen.MainContainer.route) { launchSingleTop = true } },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) { launchSingleTop = true } },
                habitViewModel = habitViewModel
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAccount = { navController.navigate(Screen.AccountSettings.route) },
                onNavigateToPrivacy = { navController.navigate(Screen.PrivacyPolicy.route) },
                onNavigateToAbout = { navController.navigate(Screen.AboutApp.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                },
                settingsViewModel = settingsViewModel,
                authViewModel = authViewModel
            )
        }

        composable(Screen.AccountSettings.route) {
            AccountSettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) { popUpTo(0) { inclusive = true } }
                },
                settingsViewModel = settingsViewModel
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable(Screen.AboutApp.route) {
            AboutAppScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}
