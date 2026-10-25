package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.viewmodel.HabitViewModel

@Composable
fun CreateHabitScreen(
    onNavigateBack: () -> Unit,
    onHabitCreated: () -> Unit,
    habitViewModel: HabitViewModel
) {
    var habitName by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("15") }
    var frequency by remember { mutableStateOf("DAILY") }
    var reminderEnabled by remember { mutableStateOf(true) }
    var nameError by remember { mutableStateOf(false) }

    Scaffold(containerColor = Color(0xFFF2F5F8)) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(40.dp).background(Color.White, CircleShape)
                ) { Icon(Icons.Default.Close, null, tint = TextPrimary) }
                Spacer(Modifier.weight(1f))
                Text("New Habit", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = PrimaryBlue)
                Spacer(Modifier.weight(1f))
                Box(Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFCDD8E0)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, null, tint = TextSecondary, modifier = Modifier.size(22.dp))
                }
            }

            Column(Modifier.padding(horizontal = 20.dp)) {
                Spacer(Modifier.height(8.dp))
                Text("INTENTIONALITY", style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp), color = AccentGreen)
                Spacer(Modifier.height(8.dp))
                Text("What shall we nurture\ntoday?", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                Spacer(Modifier.height(32.dp))

                // Habit name
                Text("Habit Name", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = habitName,
                    onValueChange = { habitName = it; nameError = false },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g., Morning Meditation", color = TextHint) },
                    trailingIcon = { Icon(Icons.Default.Edit, null, tint = TextSecondary) },
                    isError = nameError,
                    supportingText = if (nameError) { { Text("Please enter a habit name", color = ErrorRed) } } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color.Transparent,
                        errorBorderColor = ErrorRed,
                        focusedContainerColor = Color.White, unfocusedContainerColor = Color(0xFFECF0F4)
                    )
                )
                Spacer(Modifier.height(24.dp))

                // Duration
                Text("Duration (minutes)", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = duration,
                    onValueChange = { if (it.all { char -> char.isDigit() }) duration = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("15", color = TextHint) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue, unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.White, unfocusedContainerColor = Color(0xFFECF0F4)
                    )
                )
                Spacer(Modifier.height(24.dp))

                // Frequency
                Text("Frequency", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FrequencyChip("DAILY", Icons.Default.CalendarToday, frequency, Modifier.weight(1f)) { frequency = "DAILY" }
                    FrequencyChip("WEEKLY", Icons.Default.CalendarMonth, frequency, Modifier.weight(1f)) { frequency = "WEEKLY" }
                }
                Spacer(Modifier.height(24.dp))

                // Compound Effect card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentGreenLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier.size(44.dp).clip(CircleShape).background(AccentGreen),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(22.dp)) }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text("The Compound Effect", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color(0xFF1A4D37))
                            Spacer(Modifier.height(5.dp))
                            Text("Small, consistent actions lead to significant transformation over time. Start small, stay steady.", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF2D6A4F))
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))

                // Smart Reminder
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(Modifier.padding(horizontal = 18.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFEEF4F8)),
                            contentAlignment = Alignment.Center
                        ) { Icon(Icons.Default.Notifications, null, tint = PrimaryBlue, modifier = Modifier.size(24.dp)) }
                        Spacer(Modifier.width(14.dp))
                        Column(Modifier.weight(1f)) {
                            Text("Smart Reminder", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                            Text("Notify me at 8:00 AM", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = { reminderEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
                        )
                    }
                }
                Spacer(Modifier.height(36.dp))

                // Create button
                Button(
                    onClick = {
                        if (habitName.isBlank()) { nameError = true; return@Button }
                        val dur = duration.toIntOrNull() ?: 15
                        habitViewModel.addHabit(habitName.trim(), frequency, dur, reminderEnabled, "08:00")
                        onHabitCreated()
                    },
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Create Habit", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    Spacer(Modifier.width(10.dp))
                    Icon(Icons.Default.Bolt, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun FrequencyChip(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, selected: String, modifier: Modifier, onClick: () -> Unit) {
    val isSelected = selected == label
    Box(
        modifier = modifier.height(52.dp).clip(RoundedCornerShape(50))
            .background(if (isSelected) PrimaryBlueLight else Color(0xFFECF0F4))
            .then(if (isSelected) Modifier.border(0.dp, Color.Transparent, RoundedCornerShape(50)) else Modifier)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = if (isSelected) Color.White else TextSecondary, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(label.replaceFirstChar { it.uppercase() }.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                color = if (isSelected) Color.White else TextSecondary)
        }
    }
}
