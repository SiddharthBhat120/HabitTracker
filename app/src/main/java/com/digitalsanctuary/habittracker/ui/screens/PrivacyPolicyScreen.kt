package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digitalsanctuary.habittracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            TopAppBar(
                title = { Text("Settings", style = MaterialTheme.typography.bodyLarge, color = TextHint) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = PrimaryBlue)
                    }
                },
                actions = {
                    Text("HabitSync", modifier = Modifier.padding(end = 16.dp), style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = PrimaryBlue)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            Text("LEGAL FRAMEWORK", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp), color = TextHint)
            Text("Privacy Policy", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
            Spacer(Modifier.height(16.dp))
            Text(
                "Your focus is your most valuable asset. We are committed to protecting the data that helps you build a better version of yourself.",
                style = MaterialTheme.typography.bodyLarge, color = TextSecondary
            )
            
            Spacer(Modifier.height(32.dp))
            
            // Philosophy Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE3F2FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Security, null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text("Our Philosophy", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Text(
                            "We operate on a \"Privacy by Design\" principle. We only collect the bare minimum required to ensure your habit tracking is consistent across your devices.",
                            style = MaterialTheme.typography.bodySmall, color = TextSecondary
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(40.dp))
            
            PolicySection(
                number = "01",
                title = "Data Collection",
                description = "We collect information you provide directly to us when you create an account, such as your email address and name. Additionally, we track:",
                items = listOf(
                    "Habit names, frequencies, and completion timestamps.",
                    "Device type and operating system version for optimization.",
                    "Local time zone to ensure accurate habit resetting."
                )
            )
            
            Spacer(Modifier.height(40.dp))
            
            PolicySection(
                number = "02",
                title = "User Rights",
                description = "You retain full ownership of your personal growth data. Under various international privacy laws, you have the right to:",
                items = emptyList()
            )
            
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9).copy(alpha = 0.5f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Access & Export", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Text("Download a copy of all your logged habits at any time in CSV format.", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9).copy(alpha = 0.5f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Right to Erasure", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Text("Delete your account and all associated data permanently from our servers.", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                    }
                }
            }
            
            Spacer(Modifier.height(40.dp))
            
            PolicySection(
                number = "03",
                title = "Security",
                description = "We employ industry-standard encryption protocols (AES-256) to ensure your data remains secure while in transit and at rest.",
                items = emptyList()
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F1))
            ) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Local-First Storage", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF2E7D32))
                        Text(
                            "Most of your habit data is stored locally on your device and only synced to the cloud if you enable 'Remote Backup'.",
                            style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32).copy(alpha = 0.8f)
                        )
                    }
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(24.dp))
                }
            }
            
            Spacer(Modifier.height(48.dp))
            HorizontalDivider(Modifier.width(40.dp).align(Alignment.CenterHorizontally), thickness = 4.dp, color = Color(0xFFE2E8F0))
            Spacer(Modifier.height(24.dp))
            Text(
                "\"Habit is the intersection of knowledge, skill, and desire. We protect that intersection.\"",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = TextSecondary, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(64.dp))
        }
    }
}

@Composable
private fun PolicySection(number: String, title: String, description: String, items: List<String>) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(number, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = PrimaryBlue.copy(alpha = 0.15f))
            Spacer(Modifier.width(12.dp))
            Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        }
        Spacer(Modifier.height(12.dp))
        Text(description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        if (items.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            items.forEach { item ->
                Row(Modifier.padding(bottom = 12.dp)) {
                    Icon(Icons.Default.CheckCircle, null, tint = AccentGreen, modifier = Modifier.size(16.dp).offset(y = 2.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(item, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
        }
    }
}
