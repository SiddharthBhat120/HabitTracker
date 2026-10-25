package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digitalsanctuary.habittracker.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val description: String,
    val gradient: List<Color>
)

private val pages = listOf(
    OnboardingPage(
        icon = Icons.Default.AutoAwesome,
        title = "Your Digital\nSanctuary",
        subtitle = "MINDFUL LIVING",
        description = "A peaceful space to build habits that transform your everyday life through consistency and intentionality.",
        gradient = listOf(Color(0xFFD6EAF5), Color(0xFFEEF4F8))
    ),
    OnboardingPage(
        icon = Icons.Default.TrackChanges,
        title = "Track Every\nHabit",
        subtitle = "STAY CONSISTENT",
        description = "Visualise your progress with beautiful streaks, weekly overviews, and consistency scores that keep you motivated.",
        gradient = listOf(Color(0xFFE8F5EE), Color(0xFFF2F8F5))
    ),
    OnboardingPage(
        icon = Icons.Default.Notifications,
        title = "Smart\nReminders",
        subtitle = "NEVER MISS A DAY",
        description = "Gentle nudges at the right time help you stay on track. Build the compound effect — one day at a time.",
        gradient = listOf(Color(0xFFF0F0F8), Color(0xFFF5F2F8))
    )
)

@Composable
fun OnboardingScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            val p = pages[page]
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(Brush.verticalGradient(p.gradient)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(80.dp))
                    Box(
                        modifier = Modifier.size(140.dp).clip(RoundedCornerShape(40.dp))
                            .background(Color.White.copy(alpha = 0.85f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(p.icon, null, tint = PrimaryBlue, modifier = Modifier.size(70.dp))
                    }
                    Spacer(Modifier.height(48.dp))
                    Text(
                        p.subtitle,
                        style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 3.sp),
                        color = AccentGreen
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        p.title,
                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        lineHeight = 38.sp
                    )
                    Spacer(Modifier.height(20.dp))
                    Text(
                        p.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp
                    )
                }
            }
        }

        // Bottom controls
        Column(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 52.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Dots
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(pages.size) { i ->
                    val selected = pagerState.currentPage == i
                    val w by animateFloatAsState(if (selected) 28f else 8f, tween(300), label = "dot")
                    val color by animateColorAsState(
                        if (selected) PrimaryBlue else PrimaryBlue.copy(alpha = 0.25f), tween(300), label = "dotC"
                    )
                    Box(modifier = Modifier.height(8.dp).width(w.dp).clip(CircleShape).background(color))
                }
            }
            Spacer(Modifier.height(36.dp))
            if (pagerState.currentPage < pages.size - 1) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onFinished) {
                        Text("Skip", color = TextSecondary, style = MaterialTheme.typography.titleMedium)
                    }
                    Button(
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier.height(52.dp).width(130.dp)
                    ) {
                        Text("Next", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = Color.White)
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }
            } else {
                Button(
                    onClick = onFinished,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).height(56.dp)
                ) {
                    Text("Begin Your Journey", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, null, tint = Color.White)
                }
            }
        }
    }
}
