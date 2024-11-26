package io.hvk.noteappdel.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.hvk.noteappdel.data.Note
import io.hvk.noteappdel.data.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {
    val notes = repository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isAddingNote = MutableStateFlow(false)
    val isAddingNote = _isAddingNote

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote = _selectedNote

    private val _selectedNoteForDetails = MutableStateFlow<Note?>(null)
    val selectedNoteForDetails = _selectedNoteForDetails

    private val _isSelectionMode = MutableStateFlow(false)
    val isSelectionMode = _isSelectionMode

    private val _selectedNotes = MutableStateFlow<Set<Note>>(emptySet())
    val selectedNotes = _selectedNotes

    private val _showDeleteDialog = MutableStateFlow<Note?>(null)
    val showDeleteDialog = _showDeleteDialog

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage = _toastMessage

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            try {
                repository.insertNote(
                    Note(
                        title = title,
                        content = content
                    )
                )
                _isAddingNote.value = false
                showToast("Note added successfully")
            } catch (e: Exception) {
                showToast("Failed to add note: ${e.localizedMessage}")
            }
        }
    }

    fun updateNote(title: String, content: String) {
        viewModelScope.launch {
            _selectedNote.value?.let { note ->
                try {
                    repository.updateNote(
                        note.copy(
                            title = title,
                            content = content,
                            timestamp = System.currentTimeMillis()
                        )
                    )
                    showToast("Note updated successfully")
                } catch (e: Exception) {
                    showToast("Failed to update note: ${e.localizedMessage}")
                } finally {
                    clearSelectedNote()
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
                _showDeleteDialog.value = null
                showToast("Note deleted successfully")
            } catch (e: Exception) {
                showToast("Failed to delete note: ${e.localizedMessage}")
            }
        }
    }

    fun toggleAddingNote() {
        _isAddingNote.value = !_isAddingNote.value
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    fun clearSelectedNote() {
        _selectedNote.value = null
    }

    fun showNoteDetails(note: Note) {
        _selectedNoteForDetails.value = note
    }

    fun clearNoteDetails() {
        _selectedNoteForDetails.value = null
    }

    fun generateDummyNotes() {
        viewModelScope.launch {
            try {
                val dummyTitles = listOf(
                    "Meeting Notes", "Shopping List", "Ideas", "Todo List", "Project Plan",
                    "Recipe", "Book Notes", "Movie Review", "Travel Plans", "Goals"
                )
                val dummyContents = listOf(
                    "Discuss project timeline", "Buy groceries for the week", "New app features",
                    "Complete pending tasks", "Plan next sprint", "Delicious pasta recipe",
                    "Chapter summary", "Great movie must watch again", "Visit Paris next summer",
                    "Learn new programming language"
                )

                repeat(100) {
                    val title = "${dummyTitles.random()} ${(1..999).random()}"
                    val content = buildString {
                        repeat((1..5).random()) {
                            append(dummyContents.random())
                            append("\n\n")
                        }
                    }.trim()

                    repository.insertNote(
                        Note(
                            title = title,
                            content = content,
                            timestamp = System.currentTimeMillis() - (0..1000000000L).random()
                        )
                    )
                }
                showToast("100 demo notes added successfully")
            } catch (e: Exception) {
                showToast("Failed to generate demo notes: ${e.localizedMessage}")
            }
        }
    }

    fun toggleSelectionMode() {
        _isSelectionMode.value = !_isSelectionMode.value
        if (!_isSelectionMode.value) {
            _selectedNotes.value = emptySet()
        }
    }

    fun toggleNoteSelection(note: Note) {
        _selectedNotes.value = if (_selectedNotes.value.contains(note)) {
            _selectedNotes.value - note
        } else {
            _selectedNotes.value + note
        }
        
        if (_selectedNotes.value.isEmpty()) {
            _isSelectionMode.value = false
        }
    }

    fun deleteSelectedNotes() {
        viewModelScope.launch {
            try {
                _selectedNotes.value.forEach { note ->
                    repository.deleteNote(note)
                }
                _selectedNotes.value = emptySet()
                _isSelectionMode.value = false
                showToast("Selected notes deleted successfully")
            } catch (e: Exception) {
                showToast("Failed to delete selected notes: ${e.localizedMessage}")
            }
        }
    }

    fun showDeleteConfirmation(note: Note) {
        _showDeleteDialog.value = note
    }

    fun dismissDeleteDialog() {
        _showDeleteDialog.value = null
    }

    private fun showToast(message: String) {
        _toastMessage.value = message
        viewModelScope.launch {
            delay(2000)
            _toastMessage.value = null
        }
    }
} 