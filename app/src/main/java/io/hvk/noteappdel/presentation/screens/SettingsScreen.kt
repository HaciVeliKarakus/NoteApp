package io.hvk.noteappdel.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.hvk.noteappdel.R
import io.hvk.noteappdel.presentation.NotesViewModel
import io.hvk.noteappdel.presentation.ThemeViewModel

@Composable
fun SettingsScreen(
    notesViewModel: NotesViewModel,
    themeViewModel: ThemeViewModel
) {
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    var isGeneratingNotes by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Dark Theme Toggle
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Dark Mode",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Dark Theme",
                                fontSize = 16.sp
                            )
                        }
                        Switch(
                            checked = isDarkTheme,
                            onCheckedChange = { themeViewModel.toggleTheme() }
                        )
                    }

                    HorizontalDivider()

                    // Generate Demo Notes
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = "Add Dummy Notes",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Column {
                                Text(
                                    text = "Generate Demo Notes",
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Add 100 random notes for testing",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Button(
                            onClick = {
                                isGeneratingNotes = true
                                notesViewModel.generateDummyNotes()
                                isGeneratingNotes = false
                            },
                            enabled = !isGeneratingNotes
                        ) {
                            Text(if (isGeneratingNotes) "Generating..." else "Generate")
                        }
                    }
                }
            }
        }
    }
} 