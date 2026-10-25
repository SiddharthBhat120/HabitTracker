package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.digitalsanctuary.habittracker.data.local.entity.HabitEntity
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.utils.DateUtils
import com.digitalsanctuary.habittracker.viewmodel.AuthViewModel
import com.digitalsanctuary.habittracker.viewmodel.HabitViewModel
import com.digitalsanctuary.habittracker.viewmodel.SettingsViewModel

@Composable
fun HomeScreen(
    onNavigateToCreateHabit: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onOpenDrawer: () -> Unit,
    habitViewModel: HabitViewModel,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel
) {
    val habits by habitViewModel.allHabits.collectAsStateWithLifecycle()
    val completionsToday by habitViewModel.completionsToday.collectAsStateWithLifecycle()
    val authState by authViewModel.state.collectAsStateWithLifecycle()
    val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()
    
    val totalCount = habits.size
    val completedCount = completionsToday.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    val activeHabits = habits.filter { it.id !in completionsToday }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreateHabit,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.size(60.dp)
            ) { Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(28.dp)) }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, null, tint = MaterialTheme.colorScheme.onBackground, modifier = Modifier.size(26.dp))
                    }
                    Text("Digital Sanctuary", style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold))
                    Box(
                        modifier = Modifier.size(42.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant).clickable { onNavigateToSettings() },
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
                            Icon(Icons.Default.Person, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
            item {
                Column(Modifier.padding(horizontal = 20.dp)) {
                    val userName = authState.userName.ifEmpty { "User" }
                    Text(DateUtils.getCurrentDateString(), style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("${DateUtils.getGreeting()}, $userName", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(20.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Daily Focus", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onPrimaryContainer)
                                Spacer(Modifier.height(4.dp))
                                Text("$completedCount of $totalCount habits completed", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            CircularProgressWidget(progress = progress, size = 68.dp)
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    Text("Active Habits", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onBackground)
                    Spacer(Modifier.height(12.dp))
                }
            }
            
            if (activeHabits.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(64.dp))
                            Spacer(Modifier.height(12.dp))
                            Text(if (completionsToday.isNotEmpty()) "All habits done!" else "No habits yet", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(6.dp))
                            Text(if (completionsToday.isNotEmpty()) "See you tomorrow!" else "Tap + to create your first habit", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
                        }
                    }
                }
            } else {
                items(activeHabits, key = { it.id }) { habit ->
                    HabitListItem(
                        habit = habit,
                        onToggle = { habitViewModel.toggleHabit(habit.id) },
                        onDelete = { habitViewModel.deleteHabit(habit) },
                        modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CircularProgressWidget(progress: Float, size: androidx.compose.ui.unit.Dp) {
    val animProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(800), label = "prog")
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(Modifier.size(size), contentAlignment = Alignment.Center) {
        androidx.compose.foundation.Canvas(Modifier.size(size)) {
            val stroke = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            drawArc(color = primaryColor.copy(alpha = 0.18f), startAngle = -90f, sweepAngle = 360f, useCenter = false, style = stroke)
            drawArc(color = primaryColor, startAngle = -90f, sweepAngle = 360f * animProgress, useCenter = false, style = stroke)
        }
        Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = primaryColor)
    }
}

@Composable
fun HabitListItem(habit: HabitEntity, onToggle: () -> Unit, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Habit") },
            text = { Text("Are you sure you want to delete '${habit.name}'?") },
            confirmButton = {
                TextButton(onClick = { onDelete(); showDeleteDialog = false }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(46.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(getHabitIcon(habit.iconName), null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(habit.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(3.dp))
                Text(
                    "${habit.duration} mins • ${habit.currentStreak} day streak",
                    style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            IconButton(onClick = { showDeleteDialog = true }) {
                Icon(Icons.Default.DeleteOutline, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(24.dp))
            }
            
            IconButton(onClick = onToggle, modifier = Modifier.size(34.dp)) {
                Icon(Icons.Outlined.CheckCircle, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(30.dp))
            }
        }
    }
}

@Composable
fun getHabitIcon(name: String): ImageVector = when (name) {
    "self_improvement" -> Icons.Default.SelfImprovement
    "menu_book"        -> Icons.Default.MenuBook
    "fitness_center"   -> Icons.Default.FitnessCenter
    "directions_run"   -> Icons.Default.DirectionsRun
    "water_drop"       -> Icons.Default.WaterDrop
    "bedtime"          -> Icons.Default.Bedtime
    "edit_note"        -> Icons.Default.EditNote
    "work"             -> Icons.Default.Work
    "school"           -> Icons.Default.School
    else               -> Icons.Default.CheckCircle
}
