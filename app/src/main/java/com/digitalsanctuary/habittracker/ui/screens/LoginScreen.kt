package com.digitalsanctuary.habittracker.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToForgotPassword: () -> Unit,
    onLoginSuccess: () -> Unit,
    authViewModel: AuthViewModel
) {
    val state by authViewModel.state.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(state.navigateToHome) {
        if (state.navigateToHome) {
            onLoginSuccess()
            authViewModel.clearNavigationFlags()
        }
    }

    LaunchedEffect(state.error) {
        if (state.error == "User not found. Please register first") {
            onNavigateToRegister()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFD6EAF5), Color(0xFFF2F5F8), Color(0xFFEEF4F8)))
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(72.dp))
            Box(
                modifier = Modifier.size(88.dp).clip(CircleShape)
                    .background(Brush.linearGradient(listOf(PrimaryBlue, AccentGreen))),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(44.dp))
            }
            Spacer(Modifier.height(24.dp))
            Text("Digital Sanctuary", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary, textAlign = TextAlign.Center)
            Spacer(Modifier.height(44.dp))

            if (state.error != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ErrorOutline, null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(10.dp))
                        Text(state.error ?: "", color = ErrorRed, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        IconButton(onClick = { authViewModel.clearError() }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, null, tint = ErrorRed, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }

            AuthFieldLabel("EMAIL ADDRESS")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = email,
                onValueChange = { email = it; authViewModel.clearError() },
                placeholder = "hello@sanctuary.com",
                leadingIcon = { Icon(Icons.Default.Email, null, tint = TextSecondary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(Modifier.height(20.dp))

            AuthFieldLabel("PASSWORD")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = password,
                onValueChange = { password = it; authViewModel.clearError() },
                placeholder = "••••••••",
                leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextSecondary) },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null, tint = TextSecondary)
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); authViewModel.login(email, password) })
            )
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { focusManager.clearFocus(); authViewModel.login(email, password) },
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                enabled = !state.isLoading && email.isNotBlank() && password.isNotBlank()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.5.dp)
                } else {
                    Text("Sign In", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                }
            }
            Spacer(Modifier.height(28.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("New to the sanctuary?  ", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Text("Create Account", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = PrimaryBlue,
                    modifier = Modifier.clickable { onNavigateToRegister() })
            }
            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun AuthFieldLabel(text: String) =
    Text(text, style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp), color = TextSecondary, modifier = Modifier.fillMaxWidth())

@Composable
private fun AuthTextField(
    value: String, onValueChange: (String) -> Unit, placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null, trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange, modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder, color = TextHint) },
        leadingIcon = leadingIcon, trailingIcon = trailingIcon,
        visualTransformation = visualTransformation, keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions, singleLine = true, shape = RoundedCornerShape(50),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = Color.White, unfocusedContainerColor = Color(0xFFECF0F4)
        )
    )
}
