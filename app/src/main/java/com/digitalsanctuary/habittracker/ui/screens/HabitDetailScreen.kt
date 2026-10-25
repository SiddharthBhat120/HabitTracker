package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.utils.DateUtils
import com.digitalsanctuary.habittracker.viewmodel.HabitViewModel

@Composable
fun HabitDetailScreen(habitId: Long, onNavigateBack: () -> Unit, habitViewModel: HabitViewModel) {
    val habitFlow = remember(habitId) { habitViewModel.getHabitById(habitId) }
    val habit by habitFlow.collectAsStateWithLifecycle(initialValue = null)

    if (habit == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = PrimaryBlue)
        }
        return
    }

    val h = habit!!
    val progress by animateFloatAsState(
        targetValue = if (h.isCompletedToday) 1f else 0f,
        animationSpec = tween(1000), label = "prog"
    )
    val consistency = if (h.totalCompletions == 0) 0 else
        ((h.currentStreak.toFloat() / maxOf(h.currentStreak + 1, 7)) * 100).toInt().coerceIn(0, 100)

    Scaffold(containerColor = Color(0xFFF2F5F8)) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(40.dp).background(Color.White, CircleShape)
                ) { Icon(Icons.Default.ArrowBack, null, tint = TextPrimary) }
                Spacer(Modifier.weight(1f))
                Text("Digital Sanctuary", style = MaterialTheme.typography.titleMedium.copy(color = PrimaryBlue, fontWeight = FontWeight.Bold))
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, null, tint = TextSecondary) }
            }

            Column(Modifier.padding(horizontal = 20.dp)) {
                Text("MORNING ROUTINE", style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp), color = TextSecondary)
                Spacer(Modifier.height(4.dp))
                Text(h.name, style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                Spacer(Modifier.height(20.dp))

                // Circular timer card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(160.dp), contentAlignment = Alignment.Center) {
                            Canvas(Modifier.size(160.dp)) {
                                val strokeW = 14.dp.toPx()
                                val stroke = Stroke(strokeW, cap = StrokeCap.Round)
                                val inset = strokeW / 2
                                drawArc(PrimaryBlue.copy(alpha = 0.12f), -90f, 360f, false, style = stroke,
                                    topLeft = Offset(inset, inset), size = Size(size.width - strokeW, size.height - strokeW))
                                drawArc(PrimaryBlue, -90f, 360f * progress, false, style = stroke,
                                    topLeft = Offset(inset, inset), size = Size(size.width - strokeW, size.height - strokeW))
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("${h.duration}", style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                                Text("MINUTES", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp), color = TextSecondary)
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Done for today?", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                Text("Mark your progress", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            }
                            Spacer(Modifier.weight(1f))
                            Switch(
                                checked = h.isCompletedToday,
                                onCheckedChange = { habitViewModel.toggleHabit(h.id) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = PrimaryBlue)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))

                // Stats row
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Streak
                    Card(modifier = Modifier.weight(1f), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFDEEFF7)), elevation = CardDefaults.cardElevation(0.dp)) {
                        Column(Modifier.padding(16.dp)) {
                            Icon(Icons.Default.LocalFireDepartment, null, tint = StreakOrange, modifier = Modifier.size(28.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("${h.currentStreak}", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                            Text("Day Streak", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = AccentGreenLight), elevation = CardDefaults.cardElevation(0.dp)) {
                            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, null, tint = AccentGreen, modifier = Modifier.size(22.dp))
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text("$consistency%", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                                    Text("Consistency", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }
                        }
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
                            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CalendarMonth, null, tint = PrimaryBlue, modifier = Modifier.size(22.dp))
                                Spacer(Modifier.width(8.dp))
                                Column {
                                    Text(if (h.bestMonth.isNotBlank()) h.bestMonth else "—", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                    Text("Best Month", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))

                // Weekly Overview
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Weekly Overview", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Text("View History", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = PrimaryBlue)
                }
                Spacer(Modifier.height(14.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    (-5..1).forEach { offset ->
                        val isToday = offset == 0
                        val isFuture = offset > 0
                        val isPast = offset < 0
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(DateUtils.getDayAbbrev(offset), style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp), color = TextSecondary)
                            Spacer(Modifier.height(6.dp))
                            Box(
                                modifier = Modifier.size(36.dp).clip(CircleShape).background(
                                    when {
                                        isFuture -> Color.Transparent
                                        isToday && h.isCompletedToday -> AccentGreen
                                        isPast && h.currentStreak > kotlin.math.abs(offset) -> AccentGreen
                                        isToday -> Color.Transparent
                                        else -> Color.Transparent
                                    }
                                ).then(if (isToday) Modifier.clip(CircleShape) else Modifier),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isToday) {
                                    Box(Modifier.size(36.dp).clip(CircleShape)
                                        .background(Color.Transparent)
                                        .run {
                                            if (h.isCompletedToday) this.background(AccentGreen)
                                            else this.background(Color.Transparent)
                                        }, contentAlignment = Alignment.Center) {
                                        if (h.isCompletedToday)
                                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(18.dp))
                                    }
                                } else {
                                    // other days
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
