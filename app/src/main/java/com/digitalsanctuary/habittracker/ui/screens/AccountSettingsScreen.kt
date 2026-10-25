package com.digitalsanctuary.habittracker.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val context = LocalContext.current
    val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showImagePreview by remember { mutableStateOf(false) }

    var name by remember(settingsState.userName) { mutableStateOf(settingsState.userName) }
    var email by remember(settingsState.userEmail) { mutableStateOf(settingsState.userEmail) }
    var phone by remember(settingsState.phoneNumber) { mutableStateOf(settingsState.phoneNumber) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // In a real app, you should take persistable URI permission or copy the file
            settingsViewModel.setProfileImage(it.toString())
        }
    }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Account Settings", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = PrimaryBlue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(20.dp))
            
            // Profile Header
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier.size(120.dp).clip(RoundedCornerShape(45.dp))
                        .background(Color(0xFF2D3142))
                        .clickable { if (settingsState.profileImage != null) showImagePreview = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (settingsState.profileImage != null) {
                        Image(
                            painter = rememberAsyncImagePainter(settingsState.profileImage),
                            contentDescription = "Profile Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(60.dp))
                    }
                }
                
                Row(modifier = Modifier.offset(x = 12.dp, y = 12.dp)) {
                    if (settingsState.profileImage != null) {
                        IconButton(
                            onClick = { settingsViewModel.setProfileImage(null) },
                            modifier = Modifier.size(36.dp).shadow(4.dp, CircleShape)
                                .clip(CircleShape).background(Color(0xFFE57373))
                        ) {
                            Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                    }
                    IconButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.size(36.dp).shadow(4.dp, CircleShape)
                            .clip(CircleShape).background(Color(0xFF90CAF9))
                    ) {
                        Icon(Icons.Default.PhotoCamera, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            Text(settingsState.userName, style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text(if (settingsState.isPremium) "PREMIUM MEMBER" else "FREE TIER MEMBER", style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp), color = TextHint)
            
            Spacer(Modifier.height(32.dp))
            
            // Editable Fields Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(24.dp)) {
                    EditableInfoField(label = "FULL NAME", value = name, onValueChange = { name = it }, icon = Icons.Default.Person)
                    Spacer(Modifier.height(20.dp))
                    EditableInfoField(label = "EMAIL ADDRESS", value = email, onValueChange = { email = it }, icon = Icons.Default.Email, keyboardType = KeyboardType.Email)
                    Spacer(Modifier.height(20.dp))
                    EditableInfoField(label = "PHONE NUMBER", value = phone, onValueChange = { phone = it }, icon = Icons.Default.Phone, keyboardType = KeyboardType.Phone)
                    
                    Spacer(Modifier.height(32.dp))
                    
                    Button(
                        onClick = {
                            if (name.isBlank() || email.isBlank() || phone.isBlank()) {
                                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                            } else {
                                settingsViewModel.updateProfile(name, email, phone)
                                Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        enabled = name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()
                    ) {
                        Text("Save Profile", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Security Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFE3F2FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.History, null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Security", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Text("Update your login credentials", style = MaterialTheme.typography.bodySmall, color = TextHint)
                    }
                }
                Button(
                    onClick = { showPasswordDialog = true },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 20.dp).height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0))
                ) {
                    Text("Change Password", color = Color(0xFF475569), fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(Modifier.height(32.dp))
            
            TextButton(onClick = { showLogoutDialog = true }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color(0xFFE57373), modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("SIGN OUT OF ACCOUNT", style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp), color = Color(0xFFE57373))
                }
            }
            Spacer(Modifier.height(40.dp))
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to sign out of your account?") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; onLogout() }) {
                    Text("Sign Out", color = ErrorRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    if (showPasswordDialog) {
        var oldPassword by remember { mutableStateOf("") }
        var newPassword by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf<String?>(null) }

        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = oldPassword,
                        onValueChange = { oldPassword = it; passwordError = null },
                        label = { Text("Old Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        isError = passwordError != null
                    )
                    if (passwordError != null) {
                        Text(passwordError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (oldPassword == settingsState.password) {
                        if (newPassword.length < 6) {
                            passwordError = "Password too short"
                        } else {
                            settingsViewModel.setPassword(newPassword)
                            showPasswordDialog = false
                            Toast.makeText(context, "Password Updated", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        passwordError = "Incorrect old password"
                    }
                }) {
                    Text("Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPasswordDialog = false }) { Text("Cancel") }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    if (showImagePreview && settingsState.profileImage != null) {
        Dialog(onDismissRequest = { showImagePreview = false }) {
            Box(modifier = Modifier.fillMaxSize().clickable { showImagePreview = false }, contentAlignment = Alignment.Center) {
                Image(
                    painter = rememberAsyncImagePainter(settingsState.profileImage),
                    contentDescription = "Full Preview",
                    modifier = Modifier.fillMaxWidth(0.9f).aspectRatio(1f).clip(RoundedCornerShape(32.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun EditableInfoField(
    label: String, 
    value: String, 
    onValueChange: (String) -> Unit, 
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp), color = TextHint)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            leadingIcon = { Icon(icon, null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE2E8F0).copy(alpha = 0.6f),
                unfocusedContainerColor = Color(0xFFE2E8F0).copy(alpha = 0.6f),
                disabledContainerColor = Color(0xFFE2E8F0).copy(alpha = 0.6f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true
        )
    }
}
