package com.digitalsanctuary.habittracker.ui.screens

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digitalsanctuary.habittracker.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutAppScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("About App", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
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
            Spacer(Modifier.height(24.dp))
            
            // App Logo/Icon
            Box(
                modifier = Modifier.size(100.dp).shadow(12.dp, RoundedCornerShape(30.dp))
                    .clip(RoundedCornerShape(30.dp)).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(60.dp).clip(RoundedCornerShape(18.dp))
                        .background(Brush.linearGradient(listOf(PrimaryBlue, AccentGreen))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
            
            Spacer(Modifier.height(24.dp))
            Text("Bloom Habits", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = Color(0xFF1E293B))
            Spacer(Modifier.height(8.dp))
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFE2E8F0),
                modifier = Modifier.height(32.dp)
            ) {
                Box(Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.Center) {
                    Text("V2.4.0", style = MaterialTheme.typography.labelLarge, color = Color(0xFF64748B))
                }
            }
            
            Spacer(Modifier.height(40.dp))
            
            // Philosophy Card
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(Modifier.padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Our Philosophy", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "We believe that consistency is the highest form of self-love. Bloom was created to provide a digital sanctuary where you can cultivate your best self, one small ritual at a time.",
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(20.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Made with love", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold), color = PrimaryBlue)
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Action Buttons
            InfoRow(
                icon = Icons.Default.Star,
                iconBg = Color(0xFFFFF4E5),
                iconTint = Color(0xFFFF9800),
                title = "Rate Us",
                subtitle = "Spread the bloom",
                onClick = { rateApp(context) }
            )
            
            Spacer(Modifier.height(12.dp))
            
            InfoRow(
                icon = Icons.Default.SupportAgent,
                iconBg = Color(0xFFE3F2FD),
                iconTint = PrimaryBlue,
                title = "Contact Support",
                subtitle = "We're here to help",
                onClick = { contactSupport(context) }
            )
            
            Spacer(Modifier.height(32.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Privacy Policy", style = MaterialTheme.typography.labelLarge, color = Color(0xFF94A3B8))
                Spacer(Modifier.width(12.dp))
                Box(Modifier.size(4.dp).clip(CircleShape).background(Color(0xFFCBD5E1)))
                Spacer(Modifier.width(12.dp))
                Text("Terms of Use", style = MaterialTheme.typography.labelLarge, color = Color(0xFF94A3B8))
            }
            
            Spacer(Modifier.height(24.dp))
            Text("© 2024 BLOOM STUDIOS INC.", style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp), color = Color(0xFFCBD5E1))
            Spacer(Modifier.height(40.dp))
        }
    }
}

private fun rateApp(context: Context) {
    val packageName = context.packageName
    val uri = Uri.parse("market://details?id=$packageName")
    val goToMarket = Intent(Intent.ACTION_VIEW, uri)
    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
            Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    try {
        context.startActivity(goToMarket)
        Toast.makeText(context, "Opening Play Store...", Toast.LENGTH_SHORT).show()
    } catch (e: ActivityNotFoundException) {
        context.startActivity(Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
    }
}

private fun contactSupport(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf("support@yourapp.com"))
        putExtra(Intent.EXTRA_SUBJECT, "Support Request - Bloom Habits")
        putExtra(Intent.EXTRA_TEXT, "Hi, I need help with...")
    }
    try {
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        Toast.makeText(context, "Opening email app...", Toast.LENGTH_SHORT).show()
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, iconBg: Color, iconTint: Color, title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(CircleShape).background(iconBg), contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextHint)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color(0xFFCBD5E1))
        }
    }
}
