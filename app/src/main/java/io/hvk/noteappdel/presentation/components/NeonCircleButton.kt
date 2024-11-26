package io.hvk.noteappdel.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun NeonCircleButton(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "buttonAnimation")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val color = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    }
    
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.2f else 1f,
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .scale(scale)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            repeat(3) { layer ->
                Box(
                    modifier = Modifier
                        .size(56.dp - (layer * 2).dp)
                        .background(
                            color = color.copy(alpha = glowAlpha / (layer + 1)),
                            shape = CircleShape
                        )
                )
            }
        }
        
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = Color.Transparent,
            border = BorderStroke(
                width = 2.dp,
                color = color
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
} 