package com.hridaya.app.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hridaya.app.viewmodel.MoodViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    onNavigateToChat: () -> Unit,
    onNavigateToMood: () -> Unit,
    onNavigateToMusic: () -> Unit,
    onNavigateToJournal: () -> Unit,
    onNavigateToBreathe: () -> Unit,
    moodViewModel: MoodViewModel = hiltViewModel()
) {
    val latestMoods by moodViewModel.recentMoods.collectAsState()
    val latestMood = latestMoods.firstOrNull()

    val scrollState = rememberScrollState()
    val timeGreeting = remember { getTimeGreeting() }
    val currentDate = remember { SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date()) }

    val infiniteTransition = rememberInfiniteTransition(label = "bg")
    val animOffset by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(8000), RepeatMode.Reverse),
        label = "anim"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Animated background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(top = 56.dp, bottom = 32.dp)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = timeGreeting,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Hridaya 🌸",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = currentDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Mood indicator
                if (latestMood != null) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .clickable { onNavigateToMood() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = latestMood.mood.emoji, fontSize = 28.sp)
                    }
                } else {
                    FilledTonalIconButton(onClick = onNavigateToMood) {
                        Icon(Icons.Rounded.FavoriteBorder, contentDescription = "Mood")
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Hero Chat Card
            HeroChatCard(onClick = onNavigateToChat)

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions Grid
            Text(
                text = "Your Wellness Space",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Rounded.MusicNote,
                    title = "Sounds",
                    subtitle = "Ambient music",
                    gradient = listOf(Color(0xFF1A1040), Color(0xFF3D2B7A)),
                    emoji = "🎵",
                    onClick = onNavigateToMusic
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Rounded.SelfImprovement,
                    title = "Breathe",
                    subtitle = "Calm your mind",
                    gradient = listOf(Color(0xFF0A3040), Color(0xFF1A6080)),
                    emoji = "🌬️",
                    onClick = onNavigateToBreathe
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Rounded.Favorite,
                    title = "Mood",
                    subtitle = "Track how you feel",
                    gradient = listOf(Color(0xFF400A20), Color(0xFF8A2040)),
                    emoji = "💖",
                    onClick = onNavigateToMood
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Rounded.Book,
                    title = "Journal",
                    subtitle = "Write your thoughts",
                    gradient = listOf(Color(0xFF2A3010), Color(0xFF4A5820)),
                    emoji = "📝",
                    onClick = onNavigateToJournal
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Affirmation
            DailyAffirmationCard()

            Spacer(modifier = Modifier.height(20.dp))

            // Mood streak (if any)
            if (latestMoods.size >= 3) {
                MoodStreakCard(count = latestMoods.size, onClick = onNavigateToMood)
            }
        }
    }
}

@Composable
fun HeroChatCard(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.95f, targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(2000, easing = EaseInOutSine), RepeatMode.Reverse),
        label = "pulse"
    )

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "How are you feeling?",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Hridaya is here to listen, support and guide you through anything 💙",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = Color.White.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(50)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Rounded.Chat, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Text("Start talking", color = Color.White, style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "🌸", fontSize = (40 * pulse).sp)
            }
        }
    }
}

@Composable
fun QuickActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    gradient: List<Color>,
    emoji: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(gradient))
                .padding(16.dp)
        ) {
            Column {
                Text(text = emoji, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
        }
    }
}

@Composable
fun DailyAffirmationCard() {
    val affirmations = remember {
        listOf(
            "Every breath is a new beginning 🌸",
            "You are worthy of love and peace 💙",
            "This moment is enough. You are enough. ✨",
            "Healing is not linear, and that's okay 🌿",
            "Your feelings are valid. Your journey matters. 💫",
            "Small steps still move you forward 🌄",
            "Be gentle with yourself today 🕊️"
        )
    }
    val today = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
    val affirmation = remember { affirmations[today % affirmations.size] }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "✨ Today's Affirmation",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "\"$affirmation\"",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun MoodStreakCard(count: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🔥", fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$count day streak!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "You've been checking in consistently. Keep going!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

fun getTimeGreeting(): String {
    return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Good morning 🌅"
        in 12..16 -> "Good afternoon ☀️"
        in 17..20 -> "Good evening 🌇"
        else -> "Good night 🌙"
    }
}
