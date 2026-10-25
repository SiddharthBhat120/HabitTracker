package com.digitalsanctuary.habittracker.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.digitalsanctuary.habittracker.ui.theme.*
import com.digitalsanctuary.habittracker.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit,
    authViewModel: AuthViewModel
) {
    var visible by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(700), label = "alpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2200)
        val state = authViewModel.state.value
        if (state.isLoggedIn) {
            onNavigateToHome()
        } else {
            val done = authViewModel.isOnboardingDone().first()
            if (done) onNavigateToLogin() else onNavigateToOnboarding()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(Color(0xFFD6EAF5), Color(0xFFEEF4F8), Color(0xFFF2F5F8)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.scale(scale), contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier.size(110.dp).clip(RoundedCornerShape(30.dp))
                        .background(Color.White.copy(alpha = 0.92f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(72.dp).clip(CircleShape)
                            .background(Brush.linearGradient(listOf(PrimaryBlue, AccentGreen))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(40.dp))
                    }
                }
            }
            Spacer(Modifier.height(32.dp))
            Text(
                "Habit Tracker",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = TextPrimary,
                modifier = Modifier.graphicsLayer { this.alpha = alpha }
            )
            Spacer(Modifier.height(6.dp))
            Text(
                "DIGITAL SANCTUARY",
                style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 4.sp),
                color = TextSecondary,
                modifier = Modifier.graphicsLayer { this.alpha = alpha }
            )
        }
        Column(
            Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier.width(72.dp).clip(RoundedCornerShape(50)),
                color = PrimaryBlue,
                trackColor = PrimaryBlue.copy(alpha = 0.18f)
            )
            Spacer(Modifier.height(14.dp))
            Text("Entering your space...", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        }
    }
}
