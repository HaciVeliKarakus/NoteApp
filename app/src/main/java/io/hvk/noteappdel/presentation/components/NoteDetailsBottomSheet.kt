package io.hvk.noteappdel.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.hvk.noteappdel.data.Note
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

@Composable
fun NoteDetailsBottomSheet(
    note: Note,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var offsetY by remember { mutableFloatStateOf(0f) }
    val alpha by animateFloatAsState(
        targetValue = 1f - (offsetY / 1000f),
        label = "alpha"
    )

    // Determine note color based on a hash of the note's ID
    val noteColor = when (note.id % 5) {
        0 -> Color(0xFFFFFF99) // Light yellow
        1 -> Color(0xFFFFB366) // Light orange
        2 -> Color(0xFFFF99CC) // Light pink
        3 -> Color(0xFF99FF99) // Light green
        else -> Color(0xFF99CCFF) // Light blue
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f * alpha))
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        offsetY = max(0f, min(offsetY + dragAmount, 1000f))
                    },
                    onDragEnd = {
                        if (offsetY > 300) {
                            onDismiss()
                        } else {
                            offsetY = 0f
                        }
                    }
                )
            }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .offset(y = offsetY.dp)
                .alpha(alpha),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = noteColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.9f)
                    .padding(horizontal = 20.dp)
            ) {
                // Drag Handle
                Box(
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.Black.copy(alpha = 0.2f))
                        .align(Alignment.CenterHorizontally)
                )

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Note Details",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            border = BorderStroke(2.dp, Color(0xFF00FFFF)),
                            color = Color.Transparent,
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = onEdit) {
                                Icon(
                                    Icons.Outlined.Edit,
                                    contentDescription = "Edit note",
                                    tint = Color(0xFF00FFFF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            border = BorderStroke(2.dp, Color(0xFFFF00FF)),
                            color = Color.Transparent,
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = onDelete) {
                                Icon(
                                    Icons.Outlined.Delete,
                                    contentDescription = "Delete note",
                                    tint = Color(0xFFFF00FF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            border = BorderStroke(2.dp, Color.Black.copy(alpha = 0.3f)),
                            color = Color.Transparent,
                            modifier = Modifier.size(40.dp)
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = "Close",
                                    tint = Color.Black.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color.Black.copy(alpha = 0.1f)
                )

                // Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = note.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = SimpleDateFormat(
                            "MMMM dd, yyyy 'at' HH:mm",
                            Locale.getDefault()
                        ).format(Date(note.timestamp)),
                        fontSize = 14.sp,
                        color = Color.Black.copy(alpha = 0.6f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = note.content,
                        fontSize = 16.sp,
                        color = Color.Black.copy(alpha = 0.8f),
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
} 