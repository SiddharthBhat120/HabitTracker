package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.digitalsanctuary.habittracker.data.local.entity.JournalEntry
import com.digitalsanctuary.habittracker.ui.components.HomeBottomBar
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.utils.DateUtils
import com.digitalsanctuary.habittracker.viewmodel.HabitViewModel

@Composable
fun JournalScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSettings: () -> Unit,
    habitViewModel: HabitViewModel
) {
    val entries by habitViewModel.journalEntries.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF2F5F8),
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }, containerColor = PrimaryBlue, shape = CircleShape, modifier = Modifier.size(60.dp)) {
                Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
        },
        bottomBar = { HomeBottomBar(selected = 1, onHome = onNavigateToHome, onJournal = {}, onSettings = onNavigateToSettings) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(bottom = 24.dp)) {
            item {
                Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("My Journal", style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                        Text("Reflect. Grow. Remember.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                    Box(Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(PrimaryBlue.copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
                        Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    }
                }
            }
            if (entries.isEmpty()) {
                item {
                    Box(Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.EditNote, null, tint = TextHint, modifier = Modifier.size(72.dp))
                            Spacer(Modifier.height(12.dp))
                            Text("No journal entries yet", style = MaterialTheme.typography.titleLarge, color = TextSecondary)
                            Spacer(Modifier.height(6.dp))
                            Text("Tap + to write your first reflection", style = MaterialTheme.typography.bodyMedium, color = TextHint)
                        }
                    }
                }
            } else {
                items(entries, key = { it.id }) { entry ->
                    JournalCard(entry = entry, onDelete = { habitViewModel.deleteJournalEntry(entry) },
                        modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 12.dp))
                }
            }
        }
    }

    if (showAddDialog) {
        AddJournalDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, content, mood -> habitViewModel.addJournalEntry(title, content, mood); showAddDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JournalCard(entry: JournalEntry, onDelete: () -> Unit, modifier: Modifier = Modifier) {
    val moodData = moodInfo(entry.mood)
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(36.dp).clip(CircleShape).background(moodData.second.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
                    Text(moodData.first, style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(entry.title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                    Text(DateUtils.formatDate(entry.createdAt), style = MaterialTheme.typography.bodySmall, color = TextHint)
                }
                IconButton(onClick = { expanded = !expanded }, modifier = Modifier.size(32.dp)) {
                    Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = TextSecondary)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, null, tint = TextHint.copy(alpha = 0.6f), modifier = Modifier.size(18.dp))
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = DividerColor)
                    Spacer(Modifier.height(10.dp))
                    Text(entry.content, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun AddJournalDialog(onDismiss: () -> Unit, onSave: (String, String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedMood by remember { mutableStateOf("GOOD") }
    val moods = listOf("GREAT" to "🌟", "GOOD" to "😊", "NEUTRAL" to "😐", "LOW" to "😔")

    Dialog(onDismissRequest = onDismiss) {
        Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(24.dp)) {
                Text("New Journal Entry", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Spacer(Modifier.height(20.dp))
                Text("How are you feeling?", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    moods.forEach { (mood, emoji) ->
                        Box(
                            modifier = Modifier.size(44.dp).clip(CircleShape)
                                .background(if (selectedMood == mood) PrimaryBlue.copy(alpha = 0.15f) else Color(0xFFF0F4F8))
                                .run { if (selectedMood == mood) this else this },
                            contentAlignment = Alignment.Center
                        ) {
                            TextButton(onClick = { selectedMood = mood }, modifier = Modifier.fillMaxSize()) {
                                Text(emoji, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true, shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue))
                Spacer(Modifier.height(10.dp))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("What's on your mind?") },
                    modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue))
                Spacer(Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(50)) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Button(
                        onClick = { if (title.isNotBlank() && content.isNotBlank()) onSave(title, content, selectedMood) },
                        modifier = Modifier.weight(1f).height(50.dp), shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) { Text("Save", color = Color.White, fontWeight = FontWeight.Bold) }
                }
            }
        }
    }
}

private fun moodInfo(mood: String): Pair<String, Color> = when (mood) {
    "GREAT"   -> "🌟" to Color(0xFFFFD700)
    "GOOD"    -> "😊" to AccentGreen
    "NEUTRAL" -> "😐" to Color(0xFFADB5BD)
    "LOW"     -> "😔" to Color(0xFF6B7A8D)
    else      -> "😊" to AccentGreen
}
