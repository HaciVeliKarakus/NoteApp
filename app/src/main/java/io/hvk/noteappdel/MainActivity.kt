package io.hvk.noteappdel

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import io.hvk.noteappdel.data.Note
import io.hvk.noteappdel.presentation.NotesViewModel
import io.hvk.noteappdel.presentation.NotesViewModelFactory
import io.hvk.noteappdel.presentation.ThemeViewModel
import io.hvk.noteappdel.presentation.ThemeViewModelFactory
import io.hvk.noteappdel.presentation.components.DeleteConfirmationDialog
import io.hvk.noteappdel.presentation.components.NoteDetailsBottomSheet
import io.hvk.noteappdel.presentation.screens.LaunchScreen
import io.hvk.noteappdel.presentation.screens.LoginScreen
import io.hvk.noteappdel.presentation.screens.SettingsScreen
import io.hvk.noteappdel.presentation.screens.StatusScreen
import io.hvk.noteappdel.ui.theme.NoteAppDelTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        val repository = (application as NoteApp).repository
        
        setContent {
            var showLaunchScreen by remember { mutableStateOf(true) }
            
            NoteAppDelTheme {
                when {
                    showLaunchScreen -> {
                        LaunchScreen(onFinished = { showLaunchScreen = false })
                    }
                    else -> {
                        val viewModel: NotesViewModel = viewModel(
                            factory = NotesViewModelFactory(repository)
                        )
                        MainScreen(viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainScreen(viewModel: NotesViewModel) {
    val activity = LocalContext.current as Activity
    val windowSizeClass = calculateWindowSizeClass(activity)
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val themeViewModel: ThemeViewModel = viewModel(
        factory = ThemeViewModelFactory(activity)
    )
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()

    NoteAppDelTheme(darkTheme = isDarkTheme) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                bottomBar = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .height(80.dp)
                    ) {
                        // Animated border container
                        val infiniteTransition =
                            rememberInfiniteTransition(label = "borderAnimation")
                        val borderColor by infiniteTransition.animateColor(
                            initialValue = Color(0xFF00FF9C), // Neon Green
                            targetValue = Color(0xFFFF00FF),  // Neon Pink
                            animationSpec = infiniteRepeatable(
                                animation = tween(3000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "borderColor"
                        )

                        // Border
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .background(
                                    color = borderColor.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(40.dp)
                                )
                        )

                        // Content background
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp)
                                .padding(2.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(38.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Notes Button
                                NeonCircleButton(
                                    icon = Icons.Default.Dashboard,
                                    contentDescription = "Notes",
                                    isSelected = selectedTab == 0,
                                    onClick = { selectedTab = 0 }
                                )

                                // Status Button
                                NeonCircleButton(
                                    icon = Icons.Default.Lightbulb,
                                    contentDescription = "Status",
                                    isSelected = selectedTab == 1,
                                    onClick = { selectedTab = 1 }
                                )

                                // Settings Button
                                NeonCircleButton(
                                    icon = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    isSelected = selectedTab == 2,
                                    onClick = { selectedTab = 2 }
                                )
                            }
                        }
                    }
                }
            ) { padding ->
                when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        Box(modifier = Modifier.padding(padding)) {
                            when (selectedTab) {
                                0 -> NotesScreen(viewModel)
                                1 -> StatusScreen(viewModel)
                                2 -> SettingsScreen(viewModel, themeViewModel)
                            }
                        }
                    }

                    else -> {
                        Row(modifier = Modifier.padding(padding)) {
                            Box(modifier = Modifier.weight(0.6f)) {
                                NotesScreen(viewModel)
                            }
                            HorizontalDivider(
                                modifier = Modifier
                                    .width(1.dp)
                                    .fillMaxHeight()
                            )
                            Box(modifier = Modifier.weight(0.4f)) {
                                when (selectedTab) {
                                    1 -> StatusScreen(viewModel)
                                    2 -> SettingsScreen(viewModel, themeViewModel)
                                    else -> StatusScreen(viewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(viewModel: NotesViewModel) {
    val notes by viewModel.notes.collectAsState()
    val isAddingNote by viewModel.isAddingNote.collectAsState()
    val selectedNote by viewModel.selectedNote.collectAsState()
    val selectedNoteForDetails by viewModel.selectedNoteForDetails.collectAsState()
    val isSelectionMode by viewModel.isSelectionMode.collectAsState()
    val selectedNotes by viewModel.selectedNotes.collectAsState()
    val showDeleteDialog by viewModel.showDeleteDialog.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Selection mode header
            AnimatedVisibility(
                visible = isSelectionMode,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${selectedNotes.size} selected",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            IconButton(
                                onClick = { viewModel.deleteSelectedNotes() }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete selected",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                            IconButton(
                                onClick = { viewModel.toggleSelectionMode() }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Exit selection mode",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 150.dp),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 88.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalItemSpacing = 16.dp
            ) {
                items(
                    items = notes,
                    key = { note -> note.id }
                ) { note ->
                    var isVisible by remember { mutableStateOf(false) }
                    
                    LaunchedEffect(Unit) {
                        delay(300) // Small delay before animation starts
                        isVisible = true
                    }

                    AnimatedVisibility(
                        visible = isVisible,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearEasing
                            )
                        ) + slideInVertically(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearEasing
                            ),
                            initialOffsetY = { it / 2 }
                        ),
                        modifier = Modifier.animateItemPlacement(
                            animationSpec = tween(durationMillis = 300)
                        )
                    ) {
                        NoteCard(
                            note = note,
                            isSelected = selectedNotes.contains(note),
                            onDelete = { viewModel.showDeleteConfirmation(note) },
                            onEdit = { viewModel.selectNote(note) },
                            onClick = {
                                if (isSelectionMode) {
                                    viewModel.toggleNoteSelection(note)
                                } else {
                                    viewModel.showNoteDetails(note)
                                }
                            },
                            onLongClick = {
                                if (!isSelectionMode) {
                                    viewModel.toggleSelectionMode()
                                    viewModel.toggleNoteSelection(note)
                                }
                            }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            // Animated border color and rotation
            val infiniteTransition = rememberInfiniteTransition(label = "fabAnimation")
            val borderColor by infiniteTransition.animateColor(
                initialValue = Color(0xFFFF69B4),  // Light Neon Pink
                targetValue = Color(0xFFFF1493),   // Deep Pink
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "fabBorderColor"
            )
            
            val rotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = LinearEasing)
                ),
                label = "fabRotation"
            )

            // Glow effect
            val glowAlpha by infiniteTransition.animateFloat(
                initialValue = 0.2f,
                targetValue = 0.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "fabGlowAlpha"
            )

            // Outer glow layers
            repeat(3) { layer ->
                Box(
                    modifier = Modifier
                        .size(64.dp - (layer * 2).dp)
                        .align(Alignment.Center)
                        .background(
                            color = borderColor.copy(alpha = glowAlpha / (layer + 1)),
                            shape = CircleShape
                        )
                )
            }

            // Rotating border
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .rotate(rotation)
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = CircleShape
                    )
            )

            // FAB
            FloatingActionButton(
                onClick = { viewModel.toggleAddingNote() },
                modifier = Modifier
                    .size(56.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.NoteAdd,
                    contentDescription = "Add note",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        AnimatedVisibility(
            visible = isAddingNote,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                AddNoteDialog(
                    onDismiss = { viewModel.toggleAddingNote() },
                    onAdd = { title, content -> viewModel.addNote(title, content) }
                )
            }
        }

        AnimatedVisibility(
            visible = selectedNote != null,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            selectedNote?.let { note ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    EditNoteDialog(
                        note = note,
                        onDismiss = { viewModel.clearSelectedNote() },
                        onUpdate = { title, content ->
                            viewModel.updateNote(title, content)
                        }
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = selectedNoteForDetails != null,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            selectedNoteForDetails?.let { note ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    NoteDetailsBottomSheet(
                        note = note,
                        onDismiss = { viewModel.clearNoteDetails() },
                        onEdit = {
                            viewModel.clearNoteDetails()
                            viewModel.selectNote(note)
                        },
                        onDelete = {
                            viewModel.deleteNote(note)
                            viewModel.clearNoteDetails()
                        }
                    )
                }
            }
        }

        // Delete confirmation dialog
        showDeleteDialog?.let { note ->
            DeleteConfirmationDialog(
                onDismiss = { viewModel.dismissDeleteDialog() },
                onConfirm = { viewModel.deleteNote(note) }
            )
        }

        // Toast message
        AnimatedVisibility(
            visible = toastMessage != null,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 96.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 4.dp
            ) {
                Text(
                    text = toastMessage ?: "",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteCard(
    note: Note,
    isSelected: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val rotation = remember { (-2..2).random() }

    val backgroundColor = when (rotation) {
        -2 -> Color(0xFFFFFF99) // Light yellow
        -1 -> Color(0xFFFFB366) // Light orange
        0 -> Color(0xFFFF99CC) // Light pink
        1 -> Color(0xFF99FF99) // Light green
        else -> Color(0xFF99CCFF) // Light blue
    }

    val darkerColor = backgroundColor.copy(
        red = backgroundColor.red * 0.6f,
        green = backgroundColor.green * 0.6f,
        blue = backgroundColor.blue * 0.6f
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        // Selection indicator
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(4.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }

        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(2.dp)
                )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .rotate(rotation.toFloat())
                .animateContentSize()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            shape = RoundedCornerShape(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = note.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                if (note.content.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = note.content,
                        fontSize = 15.sp,
                        color = Color.Black.copy(alpha = 0.7f),
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 20.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("MMM d", Locale.getDefault())
                            .format(Date(note.timestamp)),
                        fontSize = 12.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onEdit,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = "Edit note",
                                tint = darkerColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        IconButton(
                            onClick = onDelete,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Outlined.Delete,
                                contentDescription = "Delete note",
                                tint = darkerColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .clip(MaterialTheme.shapes.extraLarge),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "New Note",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = MaterialTheme.shapes.medium,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            onAdd(title, content)
                        }
                    },
                    enabled = title.isNotBlank() && content.isNotBlank()
                ) {
                    Text("Add")
                }
            }
        }
    }
}

@Composable
fun EditNoteDialog(
    note: Note,
    onDismiss: () -> Unit,
    onUpdate: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }

    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(28.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Edit Note",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Cancel")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            onUpdate(title, content)
                        }
                    },
                    enabled = title.isNotBlank() && content.isNotBlank()
                ) {
                    Text("Update")
                }
            }
        }
    }
}

@Composable
fun AnimatedBorder(
    modifier: Modifier = Modifier,
    borderWidth: Dp = 2.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "borderAnimation")
    val color by infiniteTransition.animateColor(
        initialValue = Color(0xFF00FF9C),
        targetValue = Color(0xFFFF00FF),
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .background(
                color = color,
                shape = RoundedCornerShape(32.dp)
            )
    )
}

@Composable
fun NeonCircleButton(
    icon: ImageVector,
    contentDescription: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "buttonAnimation")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    val color = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
       Color.White
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
        // Multiple glow layers for selected state
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

        // Button surface
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