package io.hvk.noteappdel.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.hvk.noteappdel.presentation.NotesViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun StatusScreen(viewModel: NotesViewModel) {
    val notes by viewModel.notes.collectAsState()
    val lastModified = notes.maxByOrNull { it.timestamp }?.timestamp

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Notes Status",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            item {
                StatusCard(
                    title = "Total Notes",
                    value = notes.size.toString()
                )
            }

            item {
                StatusCard(
                    title = "Last Modified",
                    value = if (lastModified != null) {
                        SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                            .format(Date(lastModified))
                    } else {
                        "No notes yet"
                    }
                )
            }

            item {
                StatusCard(
                    title = "Average Note Length",
                    value = if (notes.isNotEmpty()) {
                        "${notes.map { it.content.length }.average().toInt()} characters"
                    } else {
                        "No notes yet"
                    }
                )
            }
        }
    }
}

@Composable
fun StatusCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
} 