package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digitalsanctuary.habittracker.data.local.entity.HabitEntity
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.utils.DateUtils
import com.digitalsanctuary.habittracker.viewmodel.ProgressViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressScreen(onOpenDrawer: () -> Unit, progressViewModel: ProgressViewModel) {
    val streak by progressViewModel.streak.collectAsStateWithLifecycle()
    val consistency by progressViewModel.consistency.collectAsStateWithLifecycle()
    val selectedDate by progressViewModel.selectedDate.collectAsStateWithLifecycle()
    val habitsForDate by progressViewModel.habitsForSelectedDate.collectAsStateWithLifecycle()
    
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Overall Progress", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = PrimaryBlue)
                    }
                }
            )
        },
        containerColor = Color(0xFFF2F5F8)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Row
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Total Streak
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFDEEFF7))
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Icon(Icons.Default.LocalFireDepartment, null, tint = StreakOrange, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("$streak", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                            Text("Day Streak", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                    
                    // Consistency Percentage
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = AccentGreenLight)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Icon(Icons.Default.TrendingUp, null, tint = AccentGreen, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("$consistency%", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                            Text("Consistency", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }
            }

            // Weekly Overview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Weekly Overview", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            val weekDates = DateUtils.getDatesOfWeek()
                            weekDates.forEach { date ->
                                val dateIso = DateUtils.formatDateToIso(date)
                                val isSelected = dateIso == selectedDate
                                
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable {
                                        progressViewModel.selectDate(dateIso)
                                        showBottomSheet = true
                                    }
                                ) {
                                    Text(DateUtils.getDayAbbrev(date), style = MaterialTheme.typography.labelSmall, color = if (isSelected) PrimaryBlue else TextSecondary)
                                    Spacer(Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) PrimaryBlue else Color(0xFFF1F5F9)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            DateUtils.getDayNumber(date),
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal),
                                            color = if (isSelected) Color.White else TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color.White
            ) {
                DailyHabitDetails(
                    date = selectedDate,
                    habits = habitsForDate,
                    onToggle = { habitId -> progressViewModel.toggleHabitCompletion(habitId, selectedDate) }
                )
            }
        }
    }
}

@Composable
fun DailyHabitDetails(
    date: String,
    habits: List<Pair<HabitEntity, Boolean>>,
    onToggle: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp, start = 24.dp, end = 24.dp)) {
        Text(
            text = "Habits for $date",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        Spacer(Modifier.height(16.dp))
        
        if (habits.isEmpty()) {
            Text("No habits found for this day.", color = TextSecondary)
        } else {
            habits.forEach { (habit, isCompleted) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF8FAFC))
                        .clickable { onToggle(habit.id) }
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(if (isCompleted) AccentGreenLight else Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (isCompleted) Icons.Default.Check else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (isCompleted) AccentGreen else Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(16.dp))
                    Text(
                        habit.name,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    if (isCompleted) {
                        Text("Completed", style = MaterialTheme.typography.labelMedium, color = AccentGreen)
                    }
                }
            }
        }
    }
}
