package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.digitalsanctuary.habittracker.ui.theme.*

@Composable
fun ForgotPasswordScreen(onNavigateBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFD6EAF5), Color(0xFFF2F5F8)))
        )
    ) {
        Column(Modifier.fillMaxSize().padding(horizontal = 28.dp)) {
            Spacer(Modifier.height(52.dp))
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.size(44.dp).background(Color.White.copy(alpha = 0.8f), CircleShape)
            ) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) }
            Spacer(Modifier.height(40.dp))

            if (!sent) {
                Box(
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(24.dp))
                        .background(AccentBlueLight),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.LockReset, null, tint = PrimaryBlue, modifier = Modifier.size(42.dp)) }
                Spacer(Modifier.height(24.dp))
                Text("Reset your\nPassword", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                Spacer(Modifier.height(12.dp))
                Text("Enter the email linked to your account. We'll send a reset link instantly.", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                Spacer(Modifier.height(36.dp))
                Text("EMAIL ADDRESS", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email, onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("your@email.com", color = TextHint) },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = TextSecondary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true, shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White, unfocusedContainerColor = Color(0xFFECF0F4)
                    )
                )
                Spacer(Modifier.height(32.dp))
                Button(
                    onClick = { if (email.isNotBlank()) sent = true },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Icon(Icons.Default.Send, null, tint = Color.White)
                    Spacer(Modifier.width(10.dp))
                    Text("Send Reset Link", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                }
            } else {
                Spacer(Modifier.height(40.dp))
                Box(
                    modifier = Modifier.size(100.dp).clip(CircleShape)
                        .background(AccentGreenLight),
                    contentAlignment = Alignment.Center
                ) { Icon(Icons.Default.MarkEmailRead, null, tint = AccentGreen, modifier = Modifier.size(52.dp)) }
                Spacer(Modifier.height(28.dp))
                Text("Check your\nEmail!", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                Spacer(Modifier.height(14.dp))
                Text("We've sent a password reset link to\n$email\n\nCheck your inbox and follow the instructions.", style = MaterialTheme.typography.bodyLarge, color = TextSecondary, textAlign = TextAlign.Start)
                Spacer(Modifier.height(36.dp))
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, PrimaryBlue)
                ) {
                    Text("Back to Sign In", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = PrimaryBlue)
                }
            }
        }
    }
}
