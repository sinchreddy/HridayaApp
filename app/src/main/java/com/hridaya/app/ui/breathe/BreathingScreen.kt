package com.hridaya.app.ui.breathe

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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hridaya.app.data.model.BREATHING_PATTERNS
import com.hridaya.app.data.model.BreathingPattern
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class BreathPhase { IDLE, INHALE, HOLD_IN, EXHALE, HOLD_OUT }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingScreen(onBack: () -> Unit) {
    var selectedPattern by remember { mutableStateOf(BREATHING_PATTERNS[0]) }
    var isRunning by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf(BreathPhase.IDLE) }
    var phaseProgress by remember { mutableStateOf(0f) }
    var cycleCount by remember { mutableStateOf(0) }
    var instruction by remember { mutableStateOf("Ready to begin") }
    val scope = rememberCoroutineScope()

    // Animated breath circle
    val targetScale = when (phase) {
        BreathPhase.INHALE -> 1.6f
        BreathPhase.HOLD_IN -> 1.6f
        BreathPhase.EXHALE -> 0.8f
        BreathPhase.HOLD_OUT -> 0.8f
        BreathPhase.IDLE -> 1.0f
    }
    val scale by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(
            durationMillis = when (phase) {
                BreathPhase.INHALE -> selectedPattern.inhaleSeconds * 1000
                BreathPhase.EXHALE -> selectedPattern.exhaleSeconds * 1000
                else -> 300
            },
            easing = EaseInOutSine
        ),
        label = "breathScale"
    )

    fun runBreathingCycle() {
        scope.launch {
            val pattern = selectedPattern
            while (isRunning) {
                // Inhale
                phase = BreathPhase.INHALE
                instruction = "Breathe in..."
                repeat(pattern.inhaleSeconds) {
                    if (!isRunning) return@launch
                    phaseProgress = (it + 1).toFloat() / pattern.inhaleSeconds
                    delay(1000)
                }

                // Hold in
                if (pattern.holdInSeconds > 0 && isRunning) {
                    phase = BreathPhase.HOLD_IN
                    instruction = "Hold..."
                    repeat(pattern.holdInSeconds) {
                        if (!isRunning) return@launch
                        phaseProgress = (it + 1).toFloat() / pattern.holdInSeconds
                        delay(1000)
                    }
                }

                // Exhale
                if (isRunning) {
                    phase = BreathPhase.EXHALE
                    instruction = "Breathe out..."
                    repeat(pattern.exhaleSeconds) {
                        if (!isRunning) return@launch
                        phaseProgress = (it + 1).toFloat() / pattern.exhaleSeconds
                        delay(1000)
                    }
                }

                // Hold out
                if (pattern.holdOutSeconds > 0 && isRunning) {
                    phase = BreathPhase.HOLD_OUT
                    instruction = "Hold..."
                    repeat(pattern.holdOutSeconds) {
                        if (!isRunning) return@launch
                        phaseProgress = (it + 1).toFloat() / pattern.holdOutSeconds
                        delay(1000)
                    }
                }

                if (isRunning) {
                    cycleCount++
                    phaseProgress = 0f
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Breathing Exercise", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = {
                        isRunning = false
                        phase = BreathPhase.IDLE
                        onBack()
                    }) {
                        Icon(Icons.Rounded.ArrowBackIos, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Pattern selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BREATHING_PATTERNS.forEach { pattern ->
                    FilterChip(
                        selected = selectedPattern == pattern,
                        onClick = {
                            if (!isRunning) {
                                selectedPattern = pattern
                                phase = BreathPhase.IDLE
                                cycleCount = 0
                            }
                        },
                        label = { Text("${pattern.emoji} ${pattern.name}") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Pattern description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        selectedPattern.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        selectedPattern.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        BreathStep("In", selectedPattern.inhaleSeconds)
                        if (selectedPattern.holdInSeconds > 0) BreathStep("Hold", selectedPattern.holdInSeconds)
                        BreathStep("Out", selectedPattern.exhaleSeconds)
                        if (selectedPattern.holdOutSeconds > 0) BreathStep("Hold", selectedPattern.holdOutSeconds)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main breathing circle
            Box(
                modifier = Modifier.size(280.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow rings
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .size((200 + i * 30).dp)
                            .scale(if (phase == BreathPhase.INHALE || phase == BreathPhase.HOLD_IN) scale * 0.9f else 0.9f)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = (0.1f - i * 0.03f).coerceAtLeast(0f))
                            )
                    )
                }
                // Main circle
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            when (phase) {
                                BreathPhase.INHALE -> "🌬️"
                                BreathPhase.HOLD_IN, BreathPhase.HOLD_OUT -> "✋"
                                BreathPhase.EXHALE -> "💨"
                                BreathPhase.IDLE -> selectedPattern.emoji
                            },
                            fontSize = 32.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Instruction
            Text(
                text = instruction,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )

            if (isRunning) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { phaseProgress },
                    modifier = Modifier
                        .width(200.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (cycleCount > 0) {
                Text(
                    "$cycleCount cycles completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controls
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                if (isRunning) {
                    OutlinedButton(
                        onClick = {
                            isRunning = false
                            phase = BreathPhase.IDLE
                            instruction = "Paused. Ready when you are."
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(Icons.Rounded.Pause, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pause")
                    }
                    OutlinedButton(
                        onClick = {
                            isRunning = false
                            phase = BreathPhase.IDLE
                            cycleCount = 0
                            instruction = "Ready to begin"
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(56.dp)
                    ) {
                        Icon(Icons.Rounded.Stop, null)
                    }
                } else {
                    Button(
                        onClick = {
                            isRunning = true
                            cycleCount = if (phase == BreathPhase.IDLE && instruction == "Ready to begin") 0 else cycleCount
                            runBreathingCycle()
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(56.dp).widthIn(min = 180.dp)
                    ) {
                        Icon(Icons.Rounded.PlayArrow, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Begin", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun BreathStep(label: String, seconds: Int) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("${seconds}s", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
    }
}
