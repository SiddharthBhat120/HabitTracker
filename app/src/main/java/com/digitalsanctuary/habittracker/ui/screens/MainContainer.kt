package com.digitalsanctuary.habittracker.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.digitalsanctuary.habittracker.navigation.Screen
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.viewmodel.AuthViewModel
import com.digitalsanctuary.habittracker.viewmodel.HabitViewModel
import com.digitalsanctuary.habittracker.viewmodel.ProgressViewModel
import com.digitalsanctuary.habittracker.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun MainContainer(
    rootNavController: NavHostController,
    authViewModel: AuthViewModel,
    habitViewModel: HabitViewModel,
    settingsViewModel: SettingsViewModel
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
    val authState by authViewModel.state.collectAsStateWithLifecycle()
    val progressViewModel: ProgressViewModel = viewModel()

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                drawerShape = RoundedCornerShape(topEnd = 32.dp, bottomEnd = 32.dp),
                modifier = Modifier.width(300.dp).fillMaxHeight()
            ) {
                DrawerHeader(
                    name = authState.userName.ifEmpty { settingsState.userName },
                    profileImage = settingsState.profileImage,
                    onProfileClick = { 
                        rootNavController.navigate(Screen.Settings.route)
                        scope.launch { drawerState.close() }
                    }
                )
                
                Spacer(Modifier.height(24.dp))
                
                DrawerMenuItem(
                    label = "Dashboard",
                    icon = Icons.Default.GridView,
                    selected = currentRoute == Screen.Home.route,
                    onClick = {
                        innerNavController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                
                DrawerMenuItem(
                    label = "Progress",
                    icon = Icons.Default.Timeline,
                    selected = currentRoute == Screen.Progress.route,
                    onClick = {
                        innerNavController.navigate(Screen.Progress.route) {
                            launchSingleTop = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )
                
                DrawerMenuItem(
                    label = "Settings",
                    icon = Icons.Default.Settings,
                    selected = false,
                    onClick = {
                        rootNavController.navigate(Screen.Settings.route)
                        scope.launch { drawerState.close() }
                    }
                )

                Spacer(Modifier.weight(1f))
                
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp), color = Color(0xFFF1F5F9))
                
                DrawerMenuItem(
                    label = "Logout",
                    icon = Icons.AutoMirrored.Filled.Logout,
                    selected = false,
                    tint = Color(0xFF94A3B8),
                    onClick = {
                        authViewModel.logout()
                        rootNavController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
                Spacer(Modifier.height(24.dp))
            }
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Dashboard, "Dashboard") },
                        label = { Text("Dashboard") },
                        selected = currentRoute == Screen.Home.route,
                        onClick = {
                            innerNavController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Home.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.BarChart, "Progress") },
                        label = { Text("Progress") },
                        selected = currentRoute == Screen.Progress.route,
                        onClick = {
                            innerNavController.navigate(Screen.Progress.route) {
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Settings, "Settings") },
                        label = { Text("Settings") },
                        selected = false,
                        onClick = { rootNavController.navigate(Screen.Settings.route) }
                    )
                }
            }
        ) { padding ->
            NavHost(
                navController = innerNavController,
                startDestination = Screen.Home.route,
                modifier = Modifier.fillMaxSize().padding(padding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onNavigateToCreateHabit = { rootNavController.navigate(Screen.CreateHabit.route) },
                        onNavigateToSettings = { rootNavController.navigate(Screen.Settings.route) },
                        onOpenDrawer = { scope.launch { drawerState.open() } },
                        habitViewModel = habitViewModel,
                        authViewModel = authViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
                composable(Screen.Progress.route) {
                    ProgressScreen(
                        onOpenDrawer = { scope.launch { drawerState.open() } },
                        progressViewModel = progressViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerHeader(
    name: String,
    profileImage: String?,
    onProfileClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFF1F5F9))
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                if (profileImage != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImage),
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(Icons.Default.Person, null, tint = PrimaryBlue, modifier = Modifier.size(40.dp))
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        Text(
            name,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = PrimaryBlueDark
        )
        Text(
            "Habit Master",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}

@Composable
fun DrawerMenuItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    tint: Color = PrimaryBlue,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) Color(0xFFF0F7FF) else Color.Transparent
    val contentColor = if (selected) PrimaryBlue else Color(0xFF64748B)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (selected) tint else contentColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = contentColor
        )
    }
}
