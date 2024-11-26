package io.hvk.noteappdel.ui.modifiers

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.ui.composed

fun Modifier.glassmorphism(
    alpha: Float = 0.5f,
    blurRadius: Int = 20,
    backgroundColor: Color = Color.White.copy(alpha = 0.2f)
) = composed {
    remember(alpha, blurRadius, backgroundColor) {
        Modifier
            .blur(blurRadius.dp)
            .background(backgroundColor)
            .alpha(alpha)
    }
} 