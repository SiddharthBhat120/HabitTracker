package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.viewmodel.AuthViewModel
import com.digitalsanctuary.habittracker.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAccount: () -> Unit,
    onNavigateToPrivacy: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onLogout: () -> Unit,
    settingsViewModel: SettingsViewModel,
    authViewModel: AuthViewModel
) {
    val settings by settingsViewModel.state.collectAsStateWithLifecycle()
    val auth by authViewModel.state.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF2F5F8),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 52.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.size(40.dp).background(Color.White, CircleShape)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = TextPrimary)
                }
                Text("Settings", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Text("Digital Sanctuary", style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = PrimaryBlue))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // Profile card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(64.dp).clip(CircleShape).background(Color(0xFFFFB347)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (settings.profileImage != null) {
                            Image(
                                painter = rememberAsyncImagePainter(settings.profileImage),
                                contentDescription = "Profile Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(36.dp))
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text(auth.userName.ifBlank { settings.userName }, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Spacer(Modifier.height(3.dp))
                        Text(auth.userEmail.ifBlank { settings.userEmail }, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Spacer(Modifier.height(6.dp))
                        if (settings.isPremium) {
                            Box(
                                modifier = Modifier.clip(RoundedCornerShape(50)).background(AccentGreenLight).padding(horizontal = 10.dp, vertical = 3.dp)
                            ) {
                                Text("PREMIUM MEMBER", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp, fontWeight = FontWeight.Bold), color = AccentGreen)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(24.dp))

            SettingsSectionLabel("PREFERENCES")
            Spacer(Modifier.height(10.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column {
                    ToggleSettingRow(
                        icon = Icons.Default.Notifications, iconBg = Color(0xFFDEEFF7), iconTint = PrimaryBlue,
                        label = "Notifications", checked = settings.notifications,
                        onCheckedChange = { settingsViewModel.setNotifications(it) }
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            SettingsSectionLabel("ACCOUNT & LEGAL")
            Spacer(Modifier.height(10.dp))
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                Column {
                    NavigationSettingRow(icon = Icons.Default.ManageAccounts, iconBg = Color(0xFFDEEFF7), iconTint = PrimaryBlue, label = "Account Settings", onClick = onNavigateToAccount)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = DividerColor)
                    NavigationSettingRow(icon = Icons.Default.Shield, iconBg = Color(0xFFDEEFF7), iconTint = PrimaryBlue, label = "Privacy Policy", onClick = onNavigateToPrivacy)
                    HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = DividerColor)
                    NavigationSettingRow(icon = Icons.Default.Info, iconBg = Color(0xFFDEEFF7), iconTint = PrimaryBlue, label = "About App", onClick = onNavigateToAbout)
                }
            }
            Spacer(Modifier.height(24.dp))

            // Logout button
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = LogoutRedBg)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = ErrorRed)
                Spacer(Modifier.width(10.dp))
                Text("Logout", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = ErrorRed)
            }
            Spacer(Modifier.height(16.dp))
            Text("DIGITAL SANCTUARY V2.4.0", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp), color = TextHint,
                modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(32.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = { Icon(Icons.AutoMirrored.Filled.Logout, null, tint = ErrorRed) },
            title = { Text("Logout", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout from your sanctuary?") },
            confirmButton = {
                Button(onClick = { showLogoutDialog = false; onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)) {
                    Text("Logout", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Composable
private fun SettingsSectionLabel(text: String) =
    Text(text, style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp), color = TextSecondary)

@Composable
private fun ToggleSettingRow(icon: ImageVector, iconBg: Color, iconTint: Color, label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(iconBg), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.titleMedium, color = TextPrimary, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue))
    }
}

@Composable
private fun NavigationSettingRow(icon: ImageVector, iconBg: Color, iconTint: Color, label: String, value: String? = null, onClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable { onClick() }.padding(horizontal = 16.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(40.dp).clip(CircleShape).background(iconBg), contentAlignment = Alignment.Center) {
            Icon(icon, null, tint = iconTint, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Text(label, style = MaterialTheme.typography.titleMedium, color = TextPrimary, modifier = Modifier.weight(1f))
        if (value != null) Text(value, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Spacer(Modifier.width(6.dp))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = TextHint, modifier = Modifier.size(20.dp))
    }
}
