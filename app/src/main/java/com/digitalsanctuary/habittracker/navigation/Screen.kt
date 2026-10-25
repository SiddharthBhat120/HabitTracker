package com.digitalsanctuary.habittracker.navigation

sealed class Screen(val route: String) {
    object Splash        : Screen("splash")
    object Onboarding    : Screen("onboarding")
    object Login         : Screen("login")
    object Register      : Screen("register")
    object ForgotPassword: Screen("forgot_password")
    object MainContainer : Screen("main_container") // Host for Drawer
    object Home          : Screen("home")
    object Progress      : Screen("progress")
    object Community     : Screen("community")
    object CreateHabit   : Screen("create_habit")
    object HabitDetail   : Screen("habit_detail/{habitId}") {
        fun createRoute(habitId: Long) = "habit_detail/$habitId"
    }
    object Journal       : Screen("journal")
    object Settings      : Screen("settings")
    object AccountSettings: Screen("account_settings")
    object PrivacyPolicy : Screen("privacy_policy")
    object AboutApp      : Screen("about_app")
}
