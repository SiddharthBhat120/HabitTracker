package com.digitalsanctuary.habittracker.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.digitalsanctuary.habittracker.ui.theme.PrimaryBlue

@Composable
fun HomeBottomBar(
    selected: Int,
    onHome: () -> Unit,
    onJournal: () -> Unit,
    onSettings: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selected == 0,
            onClick = onHome,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(26.dp)) },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = selected == 1,
            onClick = onJournal,
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Journal", modifier = Modifier.size(26.dp)) },
            label = { Text("Journal") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )
        NavigationBarItem(
            selected = selected == 2,
            onClick = onSettings,
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.size(26.dp)) },
            label = { Text("Settings") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryBlue,
                selectedTextColor = PrimaryBlue,
                indicatorColor = PrimaryBlue.copy(alpha = 0.1f)
            )
        )
    }
}
